<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main"/>
    <title>Manage Registrants</title>
    <script type="text/javascript">
        $(document).ready(function(){
            listRegistrants([]);
        });

        /**
         * renders a form for updating a registrant's data
         * @param target
         * @param fn
         * @param registrant
         */
        let renderRegistrantForm = function(target, preFn, fn, registrant) {
            let html = "<div class='tm-margin' id='select-organization'></div><br>";
            html += "<input class='form-control tm-margin' type='text' size='40' id='detail_lastName' placeholder='Enter Last Name'><span style='color:red;'>*</span><br>";
            html += "<input class='form-control tm-margin' type='text' size='30' id='detail_firstName' placeholder='Enter First Name'><span style='color:red;'>*</span><br>";
            html += "<input class='form-control tm-margin' type='text' size='40' id='detail_email' placeholder='Enter Email Address'><span style='color:red;'>*</span><br>";
            html += "<input class='form-control tm-margin' type='text' size='40' id='detail_phone' placeholder='Enter Phone Number'><br>";
            html += "<button id='registrantOk' type='button' class='btn btn-info tm-margin'>Save</button>";
            renderDialogForm(target, html);
            document.getElementById('registrantOk').onclick = fn;
            preFn(registrant);
        }

        let registrantDetail = curryFour(renderRegistrantForm);

        let listRegistrants = function(data)  {
            list("${createLink(controller:'registrant', action: 'list')}"
                , renderResults
                , {id: ${organization.id}});
        }

        let renderResults = function(results)  {
            renderEntryOffset = curriedEntries('registrant-table')
            ({
                editable: true
                , sortable: false
                , fnAdd: function(){renderRegistrantForm('registrant-detail'
                    , function(){updateRegistrant(0, document.getElementById('detail_lastName').value
                        , document.getElementById('detail_firstName').value
                        , document.getElementById('detail_email').value
                        , document.getElementById('detail_phone').value
                        , document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);}
                    , {id: 0});}
                , fnRemove: removeRegistrant
                , fnDraw: drawRegistrants
                , title: 'Registrants'
                , cols: ['Organization', 'Last Name', 'First Name', 'Email', 'Phone']
                , pgCallback: 'renderEntryOffset'
                , hRef: 'javascript:getDetails'
            })
            (results);
            renderEntryOffset(0);
        }

        let updateRegistrant = function(regId, lname, fname, email, phone, orgId)  {
            if(checkRegistrant(lname, fname, email, orgId))  {
                if(regId === 0)  {
                    add("${createLink(controller:'registrant', action: 'add')}"
                        , listRegistrants
                        , { lname: lname
                            , fname: fname
                            , email: email
                            , phone: phone
                            , pswd: 'changeMe!'
                            , organizationId: '${organization.id}'
                        });
                } else {
                    update("${createLink(controller:'registrant', action: 'update')}"
                        , listRegistrants
                        , { id: regId
                            , lname: lname
                            , fname: fname
                            , email: email
                            , phone: phone
                            , organizationId: orgId
                        });
                }
                clearForm();
            } else {
                scroll(0,0);
            }
        }

        let getDetails = function(id)  {
            get("${createLink(controller:'registrant', action: 'get')}"
                , registrantDetail('registrant-detail')(function(){updateRegistrant(id, document.getElementById('detail_lastName').value, document.getElementById('detail_firstName').value, document.getElementById('detail_email').value, document.getElementById('detail_phone').value, document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);})
                , { id: id }
            );
        }

        let removeRegistrant = function()  {
            getCheckedIds('activate', deleteRegistrants);
            getCheckedIds('deactivate', deleteRegistrants);
        }

        let checkRegistrant = function(lname, fname, email, orgId)  {
            if(lname == null || lname.length === 0) {
                setDangerStatus("<b>Last name cannot be blank.</b>");
                document.getElementById('lastName').focus();
                return false;
            }
            if(fname == null || fname.length === 0) {
                setDangerStatus("<b>First name cannot be blank.</b>");
                document.getElementById('firstName').focus();
                return false;
            }
            if(email == null || email.length === 0) {
                setDangerStatus("<b>Email cannot be blank.</b>");
                document.getElementById('emailAddr').focus();
                return false;
            }
            if(orgId == null || orgId === "0") {
                setWarningStatus("<b>You must select an organization.</b>");
                document.getElementById('orgs').focus();
                return false;
            }
            return true;
        }

        let drawRegistrants = function(obj, entry)  {
            let html = "<tr>";
            if (entry.active === true) {
                html += "<td style='width:auto;'><input type='checkbox' class='deactivate' value='" + entry.id + "'>&nbsp;ACTIVE<a class='tm-right' href='javascript:getDetails(" + entry.id + ");'><span class='glyphicon glyphicon-pencil'></span></a></td>";
            } else {
                html += "<td style='width:auto;'><input type='checkbox' class='activate' value='" + entry.id + "'>&nbsp;INACTIVE<a class='tm-right' href='javascript:getDetails(" + entry.id + ");'><span class='glyphicon glyphicon-pencil'></span></a></td>";
            }
            html += "<td>" + entry.organization.name + "</td>";
            html += "<td>" + entry.contact.lastName + "</td>";
            html += "<td>" + entry.contact.firstName + "</td>";
            html += "<td>" + entry.contact.email + "</td>";
            html += "<td>" + (entry.contact.phone != null ? entry.contact.phone : "") + "</td>";
            html += "</tr>";

            return html;
        }

        let populateForm = function (registrant)  {
            if(registrant.id === 0)  {
                selectOrganizations(0);
            } else {
                selectOrganizations(registrant.organization.id);
                document.getElementById('detail_lastName').value = registrant.contact.lastName;
                document.getElementById('detail_firstName').value = registrant.contact.firstName;
                document.getElementById('detail_email').value = registrant.contact.email;
                document.getElementById('detail_phone').value = registrant.contact.phone;
            }
            document.getElementById('detail_lastName').focus();
        }

        let clearForm = function()  {
            document.getElementById('passwordOne').value = "";
            document.getElementById('passwordTwo').value = "";
            document.getElementById('passwordThree').value = "";
        }
    </script>
</head>
<body>
<h2>Registrant</h2>
<div id="status-header"></div>
<div id="registrant-table"></div>
<div id="registrant-detail">
</div>
</body>
</html>
