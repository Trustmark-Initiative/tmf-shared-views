<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main"/>
    <title>Organization</title>
    <script type="text/javascript">
        $(document).ready(function(){
            listOrganizations([]);
        });

        /**
         * render a form for entering an organization
         * @param target
         * @param fn
         */
        let renderOrganizationForm = function(target, preFn, fn, org)  {
            let html = "<input type='text' class='tm-margin' size='40' id='org_name' placeholder='Enter Organization Name'><span style='color:red;'>*</span><br>";
            html += "<input type='text' class='tm-margin' size='40' id='org_display' placeholder='Enter Organization Display Name'><span style='color:red;'>*</span><br>";
            html += "<input type='text' class='tm-margin' size='40' id='org_url' placeholder='Enter Organization URL'><span style='color:red;'>*</span><br>";
            html += "<textarea class='tm-margin' cols='50' rows='6' id='org_desc' placeholder='Enter Organization Description'></textarea><span style='color:red;'>*</span><br>";
            html += "<button id='orgOk' type='button' class='btn btn-info tm-margin'>Save</button>";
            renderDialogForm(target, html);
            document.getElementById('orgOk').onclick = fn;
            preFn(org);
        }

        let organizationDetail = curryFour(renderOrganizationForm);

        let listOrganizations = function(data)  {
            list("${createLink(controller:'organization', action: 'list')}"
                , renderResults
                , {name: 'ALL'});
        }

        let renderResults = function(results)  {
            renderEntryOffset = curriedEntries('organization-table')
            ({
                editable: true
                , sortable: false
                , fnAdd: function(){renderOrganizationForm('organization', populateForm
                        , function(){addOrganization(document.getElementById('org_name').value
                            , document.getElementById('org_display').value
                            , document.getElementById('org_url').value
                            , document.getElementById('org_desc').value)}
                        , {id:0})}
                , fnRemove: removeOrganization
                , fnDraw: drawOrganizations
                , cols: ['Display', 'Name', 'URL', 'TM Issuer']
                , pgCallback: 'renderEntryOffset'
                , title: 'Organizations'
                , hRef: 'javascript:getDetails'
            })
            (results);
            renderEntryOffset(0);
        }

        let getDetails = function(id)  {
            get("${createLink(controller:'organization', action: 'get')}"
                , organizationDetail('organization')
                (populateForm)
                (function(){updateOrganization(id
                    , document.getElementById('org_name').value
                    , document.getElementById('org_display').value
                    , document.getElementById('org_url').value
                    , document.getElementById('org_desc').value);})
                , {id: id});
        }

        let checkOrganization = function(name, display, siteUrl, desc) {
            if (name == null || name.length === 0) {
                setDangerStatus("<b>Organization name cannot be blank.</b>");
                document.getElementById('org_name').focus();
                return false;
            }
            if (display == null || display.length === 0) {
                setDangerStatus("<b>Display name cannot be blank.</b>");
                document.getElementById('org_display').focus();
                return false;
            }
            if (siteUrl == null || siteUrl.length === 0) {
                setDangerStatus("<b>URL cannot be blank.</b>");
                document.getElementById('org_url').focus();
                return false;
            }
            if (desc == null || desc.length === 0) {
                setDangerStatus("<b>Description cannot be blank.</b>");
                document.getElementById('org_desc').focus();
                return false;
            }
            return true;
        }

        let addOrganization =  function(name, display, siteUrl, desc)  {
            if(checkOrganization(name, display, siteUrl, desc))  {
                add("${createLink(controller:'organization', action: 'add')}"
                    , listOrganizations
                    , { name: name
                      , displayName: display
                      , siteUrl: siteUrl
                      , description: desc
                      }
                    );
                clearForm();
            } else {
                scroll(0,0);
            }
        }

        let updateOrganization =  function(id, name, display, siteUrl, desc)  {
            if(checkOrganization(name, display, siteUrl, desc))  {
                update("${createLink(controller:'organization', action: 'update')}"
                    , listOrganizations
                    , {
                        id: id
                        , display: display
                        , url: siteUrl
                        , desc: desc
                    }
                );
                clearForm();
            } else {
                scroll(0,0);
            }
        }

        let removeOrganization = function()  {
            getCheckedIds('edit-organizations', function(list)  {
                update("${createLink(controller:'organization', action: 'delete')}"
                    , listOrganizations
                    , { ids: list }
                );
            });
        }

        let drawOrganizations = function(obj, entry)  {
            let html = "<tr>";
            if(obj.editable) {
                html += "<td style='width:min-content;white-space:nowrap;'><input type='checkbox' class='edit-organizations' value='" + entry.id + "'>&nbsp;&nbsp;<a class='tm-right' href='javascript:getDetails(" + entry.id + ");'><span class='glyphicon glyphicon-pencil'></span></a></td>";
            }
            html += "<td>" + entry.displayName + "</td>";
            html += "<td>" + entry.name + "</td>";
            html += "<td>" + entry.siteUrl + "</td>";
            if(entry.trustmarkProvider)  {
                html += "<td>Yes</td>";
            } else {
                html += "<td>No</td>";
            }
            html += "</tr>";
            return html;
        }

        let populateForm = function(org)  {
            if(org.id !== 0)  {
                document.getElementById('org_name').value = org.name;
                document.getElementById('org_display').value = org.displayName;
                document.getElementById('org_url').value = org.siteUrl;
                document.getElementById('org_desc').value = org.description;
            }
            document.getElementById('org_name').focus();

        }

        let clearForm = function()  {
            setSuccessStatus("<b>Successfully saved organization.</b>");
            hideIt('organization');
            scroll(0,0);
        }
    </script>
</head>

<body>
<div id="status-header"></div>
<div id="organization-table"></div>
<p><span style="color:red;">&nbsp;&nbsp;*</span> - Indicates required field.</p>
<div id="organization"></div>
</body>
</html>
