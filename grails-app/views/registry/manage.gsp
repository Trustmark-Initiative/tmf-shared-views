<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
    <meta name="layout" content="main"/>
    <title>Manage Registries</title>
    <script type="text/javascript">
        $(document).ready(function(){
            listRegistries([]);
        });

        let selectOrganizations = function(id)  {
            list("${createLink(controller:'organization', action: 'list')}"
                , curriedSelectOrganizations('select-organization')(id)
                , {name: 'ALL'});
        }

        /**
         * render a form for adding an endpoint
         */
        let renderForm = function(target, preFn,  fn, obj)  {
            let html = "<span class='tm-margin'><h4> Registries</h4></span><br>";
            html += "<div class='tm-margin' id='select-organization'></div><br>";
            html += "<input size='40' id='registryName' type='text' class='form-control tm-margin' placeholder='Enter Registry Name' /><span style='color:red;'>*</span><br>";
            html += "<input size='40' id='registryUrl' type='text' class='form-control tm-margin' placeholder='Enter Registry URL'/><span style='color:red;'>*</span><br>";
            html += "<textarea rows='6' cols='50' id='registryDesc' class='form-control tm-margin' placeholder='Enter Description'></textarea><br>";
            html += "<button id='registryOk' type='button' class='btn btn-info tm-margin'>Save</button>";
            renderDialogForm(target, html);
            document.getElementById('registryOk').onclick = fn;
            preFn(obj);
        }

        let registryDetail = curryFour(renderForm);

        let listRegistries = function(data)  {
            list("${createLink(controller:'registry', action: 'list')}"
                , renderResults
                , {id: '${organization.id}'});
        }

        let renderResults = function(results)  {
            renderEntryOffset = curriedEntries('registry-table')
            ({
                editable: true
                , sortable: false
                , fnAdd: function(){renderForm('registry-detail'
                    , populateForm
                    , function(){addRegistry(document.getElementById('registryName').value
                        , document.getElementById('registryUrl').value
                        , document.getElementById('registryDesc').value
                        , document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);}
                    , {id: 0});}
                , fnRemove: removeRegistry
                , fnDraw: drawRegistries
                , title: 'Registries'
                , cols: ['Name','Url','Organization']
                , pgCallback: 'renderEntryOffset'
                , hRef: 'javascript:getDetails'
            })
            (results);
            renderEntryOffset(0);
        }

        let addRegistry = function(name, url, desc, orgid)  {
            if(checkForm(name, url, desc, orgid)) {
                add("${createLink(controller:'registry', action: 'add')}"
                    , listRegistries
                    , { name: name
                        , url: url
                        , desc: desc
                        , organizationId: orgid
                    });
                clearForm();
            } else {
                scroll(0,0);
            }
        }

        let getDetails = function(id)  {
            get("${createLink(controller:'registry', action: 'get')}"
                , registryDetail('registry-detail')(populateForm)
                (function(){updateRegistry(id
                    , document.getElementById('registryName').value
                    , document.getElementById('registryUrl').value
                    , document.getElementById('registryDesc').value
                    , document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);})
                , { id: id }
            );
        }

        let removeRegistry = function()  {
            getCheckedIds('edit-registry', function(list)  {
                update("${createLink(controller:'registry', action: 'delete')}"
                    , listRegistries
                    , { ids: list }
                );
            });
        }

        let updateRegistry = function(id, name, url, desc, orgid)  {
            if(checkForm(name, url, desc, orgid)) {
                update("${createLink(controller:'registry', action: 'update')}"
                    , listRegistries
                    , { name: name
                        , url: url
                        , desc: desc
                        , organizationId: orgid
                        , id: id
                    });
                clearForm();
            } else {
                scroll(0,0);
            }
        }

        let drawRegistries = function (obj, entry)  {
            let html = "<tr>";
            if(obj.editable)  {
                html += "<td><input class='edit-registry' type='checkbox' value='"+ entry.id + "'><a class='tm-right' href='"+obj.hRef+"(" + entry.id + ");'><span class='glyphicon glyphicon-pencil'></span></a></td>";
            }
            html += "<td><b>" + entry.name + "</b></td>";
            html += "<td>" + entry.url + "</td>";
            html += "<td>" + entry.organization.name + "</td>";
            html += "</tr>";
            return html;
        }

        let checkForm = function(name, url, desc, orgId)  {
            if(name == null || name.length === 0) {
                setDangerStatus("<b>Name cannot be blank.</b>");
                document.getElementById('registryName').focus();
                return false;
            }
            if(url == null || url.length === 0) {
                setDangerStatus("<b>URL cannot be blank.</b>");
                document.getElementById('registryUrl').focus();
                return false;
            }
            if(desc == null || desc.length === 0) {
                setDangerStatus("<b>Target TIP cannot be blank.</b>");
                document.getElementById('registryDesc').focus();
                return false;
            }
            if(orgId == null || orgId === "0") {
                setDangerStatus("<b>You must select an organization.</b>");
                document.getElementById('orgs').focus();
                return false;
            }
            return true;
        }

        let populateForm = function (registry)  {
            if(registry.id === 0)  {
               selectOrganizations(0);
            } else {
                selectOrganizations(registry.organization.id);
                document.getElementById('registryName').value = registry.name;
                document.getElementById('registryUrl').value = registry.url;
                document.getElementById('registryDesc').value = registry.description;
            }
            document.getElementById('registryName').focus();
        }

        let clearForm = function()  {
            setSuccessStatus("Successfully saved registry!");
            hideIt('registry-detail')
            scroll(0,0);
        }

    </script>
</head>

<body>
<h3>Registries</h3>
<div id="status-header"></div>
<div id="registry-table"></div>
<p><span style="color:red;">&nbsp;&nbsp;*</span> - Indicates a required field.</p>
<div id="registry-detail"></div>
</body>
</html>