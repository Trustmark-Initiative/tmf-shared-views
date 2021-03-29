<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main"/>
    <title>Administer Endpoints</title>
    <script type="text/javascript">
        $(document).ready(function(){
            listEndpoints([]);
        });

        /**
         * render a form for adding an endpoint
         */
        let renderEndpointForm = function(target, preFn, fn, endpoint)  {
            let html = "<input id='endptName' size='40' type='text' class='form-control tm-margin' placeholder='Enter Name' /><span style='color:red;'>&nbsp;&nbsp;*</span><br>";
            html += "<input style='width:300px;' id='endptUrl' type='text' class='form-control' placeholder='Endpoint Url' /><br>";
            html += "<textarea cols='40' rows='8' id='signingKey' class='form-control' placeholder='Signing Key'></textarea>";
            html += "<textarea cols='40' rows='8' id='encryptionKey' class='form-control' placeholder='Encryption Key'></textarea><br>";
            html += "<input style='width:300px;' id='postBindingUrl' type='text' class='form-control' placeholder='Post Binding Url'/><br>";
            html += "<input style='width:300px;' id='redirectBindingUrl' type='text' class='form-control' placeholder='Redirect Binding Url' /><br>";
            html += "<input style='width:300px;' id='paosBindingUrl' type='text' class='form-control' placeholder='Paos Binding Url' /><br>";
            html += "Endpoint Type:&nbsp<div id='endpointType'></div><br>";
            html += "<button id='endptOk' type='button' class='btn btn-info tm-margin'>Add</button>";
            renderDialogForm(target, html);
            document.getElementById('endptOk').onclick = fn;
            preFn(endpoint);
        }

        let endpointDetail = curryFour(renderEndpointForm);

        let listEndpoints = function(data)  {
            list("${createLink(controller:'endpoint', action: 'list')}"
                , renderResults
                , {id: 'ALL'});
        }

        let renderResults = function(results)  {
            renderEntryOffset = curriedEntries('endpoints-table')
            ({
                editable: true
                , sortable: false
                , fnAdd: function(){renderEndpointForm('endpoint-details', populateForm
                    , function(){addEndpoint(document.getElementById('endptUrl').value
                            , document.getElementById('signingKey').value
                            , document.getElementById('encryptionKey').value
                            , document.getElementById('postBindingUrl').value
                            , document.getElementById('redirectBindingUrl').value
                            , document.getElementById('paosBindingUrl').value
                            , document.getElementById('types').options[getElementById('types').selectedIndex].value
                            , document.getElementById('orgs').options[getElementById('orgs').selectedIndex].value)}
                    , {id:0})}
                , fnRemove: removeEndpoint
                , fnDraw: drawEndpoints
                , cols: ['Name', 'Binding', 'URL']
                , pgCallback: 'renderEntryOffset'
                , title: 'Endpoints'
                , hRef: 'javascript:getDetails'
            })
            (results);
            renderEntryOffset(0);
        }

        let addEndpoint = function(endptUrl, signKey, encKey, postBindUrl, reBindUrl, paosBindUrl, type, orgId)  {
            add("${createLink(controller:'endpoint', action: 'add')}"
                , listEndpoints
                , { url: endptUrl
                    , signingKey: signKey
                    , encryptionKey: encKey
                    , postUrl: postBindUrl
                    , redirectUrl: reBindUrl
                    , paosUrl: paosBindUrl
                    , endptType: type
                    , organizationId: orgId
                });
            clearForm();
        }

        let removeEndpoint = function()  {
            getCheckedIds('edit-endpoints', function(list)  {
                update("${createLink(controller:'endpoint', action: 'delete')}"
                    , listEndpoints
                    , { ids: list }
                );
            });
        }

        let getDetails = function(id)  {
            get("${createLink(controller:'endpoint', action: 'get')}"
                , endpointDetail('endpoint-details')
                (populateForm)
                (function(){updateEndpoint(id
                    , document.getElementById('endptUrl').value
                    , document.getElementById('signingKey').value
                    , document.getElementById('encryptionKey').value
                    , document.getElementById('postBindingUrl').value
                    , document.getElementById('redirectBindingUrl').value
                    , document.getElementById('paosBindingUrl').value
                    , document.getElementById('types').options[getElementById('types').selectedIndex].value
                    , document.getElementById('orgs').options[getElementById('orgs').selectedIndex].value);})
                , {id: id});
        }

        let updateEndpoint =  function(id, endptUrl, signKey, encKey, postBindUrl, reBindUrl, paosBindUrl, type, orgId)  {
                update("${createLink(controller:'endpoint', action: 'update')}"
                    , listEndpoints
                    , {
                        id: id
                      , url: endptUrl
                      , signingKey: signKey
                      , encryptionKey: encKey
                      , postUrl: postBindUrl
                      , redirectUrl: reBindUrl
                      , paosUrl: paosBindUrl
                      , endptType: type
                      , organizationId: orgId
                    }
                );
                clearForm();
        }

        let drawEndpoints = function(obj, entry)  {
            let html = "<tr>";
            html += "<td><input type='checkbox' class='edit-endpoints' value='" + entry.id + "'><a class='tm-right' href='javascript:getDetails(" + entry.id + ");'><span class='glyphicon glyphicon-pencil'></span></a></td>";
            if(entry.name === "Attribute Consuming Service")  {
                html += "<td>" + entry.name + "</td>";
                html += "<td>" + entry.serviceName + "</td>";
                let names = "";
                entry.attributes.forEach(a => { if(a.name !== 'ServiceName') names += a.name+"<br>";});
                html += "<td>" + names + "</td>";
            }  else  {
                html += "<td>" + entry.name + "</td>";
                html += "<td>" + entry.binding.substring(entry.binding.lastIndexOf(":")+1) + "</td>";
                html += "<td>" + entry.url + "</td>";
            }
            html += "</tr>";
            return html;
        }

        let listTypes = function()  {
            list("${createLink(controller:'endpoint', action: 'etypes')}"
                , renderTypes
                , {id: 'ALL'});
        }

        let renderTypes = function(data)  {
            let html = "<select class='form-control' id='types'>";
            data.forEach(o => {
                html += "<option value='"+o.name+"'>"+o.name+"</option>";
            });
            html += "</select>";
            $('#endpointType').html(html);
        }

        let populateForm = function(endpoint)  {
            if(endpoint.id === 0)  {
                listTypes();
            }  else  {
                listTypes();
            }
            document.getElementById('endptName').focus();
        }

        /**
         * clear theform
         */
        let clearForm = function()  {
            document.getElementById('endptUrl').value = "";
            document.getElementById('signingKey').value = "";
            document.getElementById('encryptionKey').value = "";
            document.getElementById('postBindingUrl').value = "";
            document.getElementById('redirectBindingUrl').value = "";
            document.getElementById('paosBindingUrl').value = "";
            document.getElementById('orgs').selectedIndex = 0;
            hideIt('endpoint-details');
        }
    </script>
</head>

<body>
<h2>Endpoints</h2>
<div id="status-header"></div>
<div id="endpoints-table"></div>
<p><span style="color:red;">&nbsp;&nbsp;*</span> - Indicates required field.</p>
<div id="endpoint-details"></div>
</body>
</html>