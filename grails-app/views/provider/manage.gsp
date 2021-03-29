<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main"/>
    <title>Manage Providers</title>
<script type="text/javascript">
    $(document).ready(function(){
        listProviders([]);
    });

    let selectOrganizations = function(id)  {
        list("${createLink(controller:'organization', action: 'list')}"
            , curriedSelectOrganizations('select-organization')(id)
            , {name: 'ALL'});
    }

    let selectTypes = function(id)  {
        list("${createLink(controller:'provider', action: 'types')}"
            , curriedProviderTypes('select-provider-types')(id)
            , {name: 'ALL'});
    }

    let selectTargetTips = function(id)  {
        list("${createLink(controller:'provider', action: 'targetTips')}"
            , curriedTargetTips('target-tips')(id)
            , {id: id});
    }

    /**
     * render a form for adding an endpoint
     */
    let renderProviderForm = function(target, preFn,  fn, obj)  {
        let html = "<span class='tm-margin'><h4> Endpoints</h4></span><br>";
        html += "<b>Organization:</b>&nbsp;<div class='tm-margin' id='select-organization'></div><br>";
        html += "<b>System Type:</b>&nbsp;<div class='tm-margin' id='select-provider-types'></div><br>";
        html += "<b>Name:</b>&nbsp;<input size='40' id='providerName' type='text' class='form-control tm-margin' placeholder='Enter System Name' /><span style='color:red;'>*</span><br>";
        html += "<b>Endpoint Url:</b>&nbsp;<input size='50' id='providerEntityId' type='text' class='form-control tm-margin' placeholder='Enter Endpoint URL'/><span style='color:red;'>*</span><br>";
        html += "<div id='target-tips'></div>";
        html += "&nbsp;&nbsp;<b>Require Full Compliance?</b>&nbsp;<input id='fullyCompliant' type='checkbox' class='form-control tm-margin'/><br>";
        html += "<button id='providerOk' type='button' class='btn btn-info tm-margin'>Save</button>";
        renderDialogForm(target, html);
        document.getElementById('providerOk').onclick = fn;
        preFn(obj);
    }

    let providerDetail = curryFour(renderProviderForm);

    let listProviders = function(data)  {
        list("${createLink(controller:'provider', action: 'list')}"
            , renderResults
            , {id: '${organization.id}'});
    }

    let renderResults = function(results)  {
        renderEntryOffset = curriedEntries('provider-table')
        ({
            editable: true
            , sortable: false
            , fnAdd: function(){renderProviderForm('provider-detail'
                , populateForm
                , function(){addProvider(document.getElementById('providerName').value
                    , document.getElementById('providerEntityId').value
                    , document.getElementById('fullyCompliant').checked
                    , document.getElementById('ptypes').options[document.getElementById('ptypes').selectedIndex].value
                    , document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);}
                , {id: 0});}
            , fnRemove: removeProvider
            , fnDraw: drawProviders
            , title: 'Providers'
            , cols: ['Name', 'Endpoint URL','Type','Organization']
            , pgCallback: 'renderEntryOffset'
            , hRef: "javascript:getDetails"
            , hRef2: "javascript:linkDetails"
        })
        (results);
        renderEntryOffset(0);
    }

    let addProvider = function(name, entityId, conformance, type, orgid)  {
        if(checkForm(name, entityId, conformance, type, orgid)) {
            add("${createLink(controller:'provider', action: 'add')}"
                , listProviders
                , { name: name
                    , entityId: entityId
                    , fullyCompliant: conformance
                    , organizationId: orgid
                    , type: type
                });
            clearForm();
        } else {
            scroll(0,0);
        }
    }

    let getDetails = function(id)  {
        get("${createLink(controller:'provider', action: 'get')}"
            , providerDetail('provider-detail')(populateForm)
            (function(){updateProvider(id
                , document.getElementById('providerName').value
                , document.getElementById('providerEntityId').value
                , document.getElementById('fullyCompliant').checked
                , document.getElementById('ptypes').options[document.getElementById('ptypes').selectedIndex].value
                , document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);})
            , { id: id }
        );
    }

    let linkDetails = function(id, orgid)  {
        let assignUrl = "${createLink(controller:'provider', action: 'dashboard', params:[id: '__ID__', orgid: '__ORGID__'])}";
        assignUrl = assignUrl.replace('__ID__', id);
        assignUrl = assignUrl.replace('__ORGID__', orgid);
        window.location = assignUrl;
    }

    let removeProvider = function()  {
        getCheckedIds('edit-providers', function(list)  {
            update("${createLink(controller:'provider', action: 'delete')}"
                , listProviders
                , { ids: list }
            );
        });
    }

    let updateProvider = function(id, name, entityId, conformance, type, orgid)  {
        if(checkForm(name, entityId, conformance, type, orgid)) {
            insertTargetTips(id);
            removeTargetTips(id);

            update("${createLink(controller:'provider', action: 'update')}"
                , listProviders
                , { name: name
                    , entityId: entityId
                    , fullyCompliant: conformance
                    , organizationId: orgid
                    , type: type
                    , id: id
                });
            clearForm();
        } else {
            scroll(0,0);
        }
    }

    let drawProviders = function (obj, entry)  {
        let html = "<tr>";
        if(obj.editable)  {
            html += "<td style='width:65px;'><input class='edit-provider' type='checkbox' value='"+ entry.id + "'>";
            html += "&nbsp;<a href='"+obj.hRef2+"("+entry.id+ "," + ${organization.id}+");'><span class='glyphicon glyphicon-list-alt'></span></a>";
            html += "&nbsp;<a class='tm-right' href='"+obj.hRef+"("+entry.id+");'><span class='glyphicon glyphicon-pencil'></span></a>";
            html += "</td>";
        }
        html += "<td>" + entry.name + "</td>";
        html += "<td>" + entry.entityId + "</td>";
        if(entry.providerType.name === "SAML_SP")  {
            html += "<td>SAML&nbsp;SP</td>";
        }
        if(entry.providerType.name === "SAML_IDP")  {
            html += "<td>SAML&nbsp;IDP</td>";
        }
        html += "<td>" + entry.organization.name + "</td>";
        html += "</tr>";
        return html;
    }

    let checkForm = function(name, entityId, conformance, type, orgid)  {
        if(name == null || name.length === 0) {
            setDangerStatus("<b>Name cannot be blank.</b>");
            document.getElementById('providerName').focus();
            return false;
        }
        if(entityId == null || entityId.length === 0) {
            setDangerStatus("<b>Entity Id cannot be blank.</b>");
            document.getElementById('providerEntityId').focus();
            return false;
        }
        if(type == null || type === "0") {
            setDangerStatus("<b>You must select a type.</b>");
            document.getElementById('ptypes').focus();
            return false;
        }
        return true;
    }

    let populateForm = function (provider)  {
        if(provider.id === 0)  {
            selectOrganizations(0);
            selectTypes(0);
            selectTargetTips(0);
        } else {
            selectOrganizations(provider.organization.id);
            selectTypes(provider.providerType.name);
            selectTargetTips(provider.id);
            document.getElementById('providerName').value = provider.name;
            document.getElementById('providerEntityId').value = provider.entityId;
            if(provider.fullyCompliant)  {
                document.getElementById('fullyCompliant').checked = true;
            }
        }
        document.getElementById('providerName').focus();
    }

    let clearForm = function()  {
        setSuccessStatus("Successfully saved Endpoint!");
        hideIt('provider-detail')
        scroll(0,0);
    }

    let addTargetTip = function(id) {
        console.log('addTargetTip '+ id);
        let data = [];
        let table = document.getElementById('target-tip-table');
        for(let i=0; i < table.rows.length;++i)  {
            if(table.rows[i].cells[0].innerText !== 'There are no tips.')  {
                console.log(table.rows[i].cells[0].id +","+ table.rows[i].cells[0].innerText);
                data.push({id: table.rows[i].cells[0].id, conformanceTargetTipIdentifier: table.rows[i].cells[0].innerText});
            }
        }
        if (document.getElementById('new-ttip').value != null) {
            data.push({id: 0, conformanceTargetTipIdentifier: document.getElementById('new-ttip').value})
        }
        curriedTargetTips('target-tips')(id)(data)
    }

    let insertTargetTips = function(id)  {
        let table = document.getElementById('target-tip-table');
        for(let i=0; i < table.rows.length;++i)  {
            if(table.rows[i].cells[0].id === '0')  {
                console.log("insertTargetTips  "+table.rows[i].cells[0].id +","+ table.rows[i].cells[0].innerText);
                add("${createLink(controller:'provider', action: 'addTargetTip')}"
                    , function(){}
                    , {
                        id: id
                        , url: table.rows[i].cells[0].innerText
                    }
                );
            }
        }
    }

    let deleteTargetTip = function(id, tid)  {
        console.log('deleteTargetTip '+ id + ', '+ tid);
        let data = [];
        let table = document.getElementById('target-tip-table');
        for(let i=0; i < table.rows.length;++i)  {
            if(table.rows[i].cells[0].id != tid)  {
                console.log(table.rows[i].cells[0].id +","+ table.rows[i].cells[0].innerText);
                data.push({id: table.rows[i].cells[0].id, conformanceTargetTipIdentifier: table.rows[i].cells[0].innerText});
            }
        }
        curriedTargetTips('target-tips')(id)(data)
    }

    let removeTargetTips = function(id)  {
        console.log('removeTargetTips '+ id);
        let data = "";
        let table = document.getElementById('target-tip-table');
        for(let i=0; i < table.rows.length;++i)  {
            console.log(table.rows[i].cells[0].id +","+ table.rows[i].cells[0].innerText);
            data += table.rows[i].cells[0].id+":";
        }
        update("${createLink(controller:'provider', action: 'deleteTargetTip')}"
            , function(){}
            , {
                id: id
                , tids: data
            }
        );
    }
</script>
</head>
<body>
<h3>Systems</h3>
<div id="status-header"></div>
<div id="provider-table"></div>
<p><span style="color:red;">&nbsp;&nbsp;*</span> - Indicates a required field.</p>
<div id="provider-detail"></div>
</body>
</html>