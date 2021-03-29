<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main"/>
    <title>Contacts</title>
    <script type="text/javascript">
        $(document).ready(function(){
            listContacts([]);
        });

        /**
         * render a form for adding/upating a contact
         */
        let renderContactForm = function(target, preFn, fn, contact)  {
            let html = "<div class='tm-margin' id='select-organization'></div><br>";
            html += "<div class='tm-margin' id='select-contact-types'></div><br>";
            html += "<input id='lastName' type='text' size='40' class='form-control tm-margin' placeholder='Enter Last Name'/><span style='color:red;'>*</span><br>";
            html += "<input id='firstName' type='text' size='40' class='form-control tm-margin' placeholder='Enter First Name'/><span style='color:red;'>*</span><br>";
            html += "<input id='emailAddr' type='text' size='40' class='form-control tm-margin' placeholder='Enter Email Address'/><span style='color:red;'>*</span><br>";
            html += "<input id='phoneNbr' type='text' size='16' class='form-control tm-margin' placeholder='Enter Phone Number'/><br>";
            html += "<button id='contactOk' type='button' class='btn btn-info tm-margin'>Add</button>";
            renderDialogForm(target, html);
            document.getElementById('contactOk').onclick = fn;
            preFn(contact);
        }

        let contactDetail = curryFour(renderContactForm);

        let listContacts = function(data)  {
            list("${createLink(controller:'contact', action: 'list')}"
                , renderResults
                , {id: 0});
        }

        let renderResults = function(results)  {
            renderEntryOffset = curriedEntries('contacts-table')
            ({
                editable: true
                , sortable: false
                , fnAdd: function(){renderContactForm('contact-details', populateContactForm
                    , function(){updateContact(0, document.getElementById('lastName').value
                        , document.getElementById('firstName').value
                        , document.getElementById('emailAddr').value
                        , document.getElementById('phoneNbr').value
                        , document.getElementById('ctypes').options[document.getElementById('ctypes').selectedIndex].value
                        , document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);}
                    , {id:0});}
                , fnRemove: removeContact
                , fnDraw: drawContacts
                , cols: ['Type', 'Last Name', 'First Name', 'Email', 'Phone']
                , title: 'Contacts'
                , pgCallback: 'renderEntryOffset'
                , hRef: 'javascript:getDetails'
            })
            (results);
            renderEntryOffset(0);
        }

        let selectOrganizations = function(id)  {
            list("${createLink(controller:'organization', action: 'list')}"
                , curriedSelectOrganizations('select-organization')(id)
                , {name: 'ALL'});
        }

        let selectContactTypes = function(id)  {
            list("${createLink(controller:'contact', action: 'types')}"
                , curriedContactTypes('select-contact-types')(id)
                , {name: 'ALL'});
        }

        let getDetails = function(id)  {
            get("${createLink(controller:'contact', action: 'get')}"
                , contactDetail('contact-details')(populateContactForm)
                (function(){updateContact(id, document.getElementById('lastName').value
                    , document.getElementById('firstName').value
                    , document.getElementById('emailAddr').value
                    , document.getElementById('phoneNbr').value
                    , document.getElementById('ctypes').options[document.getElementById('ctypes').selectedIndex].value
                    , document.getElementById('orgs').options[document.getElementById('orgs').selectedIndex].value);})
                , { id: id }
            );
        }

        let removeContact = function()  {
            getCheckedIds('edit-contacts', function(list)  {
                update("${createLink(controller:'contact', action: 'delete')}"
                    , listContacts
                    , { ids: list }
                );
            });
        }

        let updateContact = function(id, lname, fname, email, phone, type, orgId)  {
            if(checkContact(lname, fname, email, phone, type, orgId))  {
                if(id === 0)  {
                    add("${createLink(controller:'contact', action: 'add')}"
                        , listContacts
                        , { lname: lname
                            , fname: fname
                            , email: email
                            , phone: phone
                            , organizationId: orgId
                            , type: type
                        });
                }  else {
                    update("${createLink(controller:'contact', action: 'update')}"
                        , listContacts
                        , {
                            id: id
                            , lname: lname
                            , fname: fname
                            , email: email
                            , phone: phone
                            , organizationId: orgId
                            , type: type
                        });
                }
                clearForm();
            } else {
                scroll(0,0);
            }
        }

        /**
         * checks the form contents for completeness
         * @param lname
         * @param fname
         * @param email
         * @param phone
         * @param type
         * @param orgId
         * @returns {boolean}
         */
        let checkContact = function(lname, fname, email, phone, type, orgId)  {
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
                setDangerStatus("<b>You must select an organization.</b>");
                document.getElementById('orgs').focus();
                return false;
            }
            if(type == null || type === "0") {
                setDangerStatus("<b>You must select a contact type.</b>");
                document.getElementById('ctypes').focus();
                return false;
            }
            return true;
        }

        let drawContacts = function(obj, entry)  {
            let html = "<tr>";
            if(obj.editable)  {
                html += "<td><input class='edit-contacts' type='checkbox' value='"+ entry.id + "'><a class='tm-right' href='"+obj.hRef+"(" + entry.id + ");'><span class='glyphicon glyphicon-pencil'></span></a></td>";
            }
            html += "<td>" + entry.type.name + "</td>";
            html += "<td>"+ entry.lastName + "</td>";
            html += "<td>" + entry.firstName + "</td>";
            html += "<td>" + entry.email + "</td>";
            html += "<td>" + (entry.phone != null ? entry.phone : "") + "</td>";
            html += "</tr>";
            return html;
        }

        let populateContactForm = function(contact) {
            if(contact.id === 0)  {
                selectOrganizations(0);
                selectContactTypes('0');
            } else {
                selectContactTypes(contact.type.name)
                selectOrganizations(contact.organization.id);
                document.getElementById('lastName').value = contact.lastName;
                document.getElementById('firstName').value = contact.firstName;
                document.getElementById('emailAddr').value = contact.email;
                document.getElementById('phoneNbr').value = contact.phone;
            }
            document.getElementById('lastName').focus();
        }

        let clearForm = function()  {
            setSuccessStatus("<b>Successfully saved contact.</b>");
            hideIt('contact-details');
            scroll(0,0);
        }
    </script>
</head>

<body>
<div id="status-header"></div>
<div id="contacts-table"></div>
<p><span style="color:red;">&nbsp;&nbsp;*</span> - Indicates required field.</p>
<div id="contact-details"></div>
</body>
</html>
