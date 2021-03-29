<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main"/>
    <title>Dashboard</title>
<script type="text/javascript">
    $(document).ready(function(){
        listProviders([]);
    });

    let listProviders = function(data)  {
        list("${createLink(controller:'provider', action: 'listByType')}"
            , renderResults
            , {id: '${organization.id}'
            , type: '${provider.providerType}'});
    }

    let renderResults = function(results)  {
        renderEntryOffset = curriedEntries('provider-table')
        ({
            editable: false
            , sortable: false
            , fnAdd: function(){}
            , fnRemove: function(){}
            , fnDraw: drawProviders
            , title: 'Endpoints'
            , cols: ['Name', 'Endpoint URL','Type','Organization']
            , pgCallback: 'renderEntryOffset'
            , hRef: "javascript:getDetails"
            , hRef2: "javascript:linkDetails"
        })
        (results);
        renderEntryOffset(0);
    }

    let drawProviders = function (obj, entry)  {
        let html = "<tr>";
        html += "<td>" + entry.name + "</td>";
        html += "<td>" + entry.entityId + "</td>";
        if(entry.providerType.name === "SAML_SP")  {
            html += "<td>SAML&nbsp;SP</td>";
        }
        if(entry.providerType.name === "SAML_IDP")  {
            html += "<td>SAML&nbsp;IP</td>";
        }
        html += "<td>" + entry.organization.name + "</td>";
        html += "</tr>";
        return html;
    }

</script>
</head>

<body>
<h3>Endpoint Dashboard</h3>
<div id="status-header"></div>
<p><span style="color:red;">&nbsp;&nbsp;*</span> - Indicates a required field.</p>
<div id="provider-table"></div>
</body>
</html>