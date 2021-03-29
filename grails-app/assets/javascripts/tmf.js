
let renderEntryOffset = function(){};
/**
 * renders a table of contacts
 * @param target
 * @param obj
 * @param data
 * @param offset
 */
let renderEntries = function(target, obj, data, offset)  {
    if(obj.pgCnt == null) {
        obj.pgCnt = MAX_DISPLAY;
    }
    if(obj.pgCallback == null) {
        obj.pgCallback = 'renderEntryOffset';
    }

    let html = renderPagination(target, offset, obj.pgCnt, data.length, obj.pgCallback);
    html += "<table class='table table-condensed table-striped table-bordered'>";
    html += "<thead><tr>";
    if(obj.editable)  {
        html += "<th style='width:min-content;white-space:nowrap;'>";
        html += "<div class='tm-left'><a id='plus-"+target+"' title='Add an Entry'><span class='glyphicon glyphicon-plus'></span></a>&nbsp;/&nbsp;<a id='minus-"+target+"' title='Remove Checked Entries'><span class='glyphicon glyphicon-minus'></span></a></div>";
        html += "</th>";
    }
    obj.cols.forEach(c => {
        if(obj.sortable) {
            html += "<th><span class='glyphicon glyphicon-chevron-down sort-"+target+"'></span>" + c + "</th>";
        } else {
            html += "<th>" + c + "</th>";
        }
    });
    html += "</tr></thead><tbody>";
    if (data.length === 0)  {
        html += "<tr><td colspan='"+(obj.cols.length+1)+"'><em>There are no entries in the system.</em></td></tr>";
    }  else {
        let idx = 0;
        data.forEach(entry => {
            if(idx >= offset && idx < offset+obj.pgCnt)  {
                html += obj.fnDraw(obj, entry);
            }
            ++idx;
        });
    }
    html += "</tbody></table>";
    document.getElementById(target).innerHTML = html;
    if(obj.sortable) {
        let sortColumns = document.getElementsByClassName('sort-'+target);
        for(let i=0; i < sortColumns.length; ++i)  {
            sortColumns[i].onclick = obj.fnSort(i, data, obj.fnRender);
        }
    }

    if(obj.editable) {
        document.getElementById('plus-'+target).onclick = obj.fnAdd;
        document.getElementById('minus-'+target).onclick = obj.fnRemove;
    }
}

/**
 * hide the passed in div
 * @param target
 */
let hideIt = function(target)  {
    if(target.startsWith('#'))  {
        document.getElementById(target.substring(1)).style.display = 'none';
    } else {
        document.getElementById(target).style.display = 'none';
    }
}

/**
 * hide the passed in div
 * @param target
 */
let showIt = function(target)  {
    if(target.startsWith('#'))  {
        document.getElementById(target.substring(1)).style.display = 'block';
    } else {
        document.getElementById(target).style.display = 'block';
    }
}

/**
 * hides or displays the target
 * @param target
 * @returns {boolean}
 */
let toggleIt = function(target)  {
    if(target.startsWith('#')) {
        target = document.getElementById(target.substring(1));
    }
    if(document.getElementById(target).style.display === 'none')  {
        document.getElementById(target).style.display = 'block';
    } else {
        document.getElementById(target).style.display = 'none';
    }
    return false;
}

/**
 * render a form for entering an organization
 * @param target
 * @param fn
 */
let renderOrganizationForm = function(target, fn, org)  {
    let html = "<input type='text' class='tm-margin' size='40' id='org_name' placeholder='Enter Organization Name'><span style='color:red;'>*</span><br>";
    html += "<input type='text' class='tm-margin' size='40' id='org_display' placeholder='Enter Organization Display Name'><span style='color:red;'>*</span><br>";
    html += "<input type='text' class='tm-margin' size='40' id='org_url' placeholder='Enter Organization URL'><span style='color:red;'>*</span><br>";
    html += "<textarea class='tm-margin' cols='50' rows='6' id='org_desc' placeholder='Enter Organization Description'></textarea><span style='color:red;'>*</span><br>";
    html += "<button id='orgOk' type='button' class='btn btn-info tm-margin'>Save</button>";
    renderDialogForm(target, html);
    document.getElementById('org_name').focus();
    document.getElementById('orgOk').onclick = fn;
    if(org.id !== 0)  {
        document.getElementById('org_name').value = org.name;
        document.getElementById('org_display').value = org.displayName;
        document.getElementById('org_url').value = org.siteUrl;
        document.getElementById('org_desc').value = org.description;
    }
    document.getElementById('org_name').focus();
}

/**
 * render a form for adding a contact
 */
let renderContactForm = function(target, preFn, fn, contact)  {
    let html = "";
    html += "<div class='tm-margin' id='select-organization'></div><br>";
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

/**
 * render a form for adding an endpoint
 */
let renderEndpointForm = function(target, fn)  {
    let html = "<input id='endptName' size='40' type='text' class='form-control tm-margin' placeholder='Enter Name' /><span style='color:red;'>&nbsp;&nbsp;*</span><br>";
    html += "<input style='width:300px;' id='endptUrl' type='text' class='form-control' placeholder='Endpoint Url' /><br>";
//    html += "<textarea cols='40' rows='8' id='signingKey' class='form-control' placeholder='Signing Key'></textarea>";
//    html += "<textarea cols='40' rows='8' id='encryptionKey' class='form-control' placeholder='Encryption Key'></textarea><br>";
    html += "<input style='width:300px;' id='postBindingUrl' type='text' class='form-control' placeholder='Post Binding Url'/>";
    html += "<input style='width:300px;' id='redirectBindingUrl' type='text' class='form-control' placeholder='Redirect Binding Url' />";
    html += "<input style='width:300px;' id='paosBindingUrl' type='text' class='form-control' placeholder='Paos Binding Url' />";
    html += "Endpoint Type:&nbsp<div id='endpointType'></div><br>";
    html += "<button id='endptOk' type='button' class='btn btn-info tm-margin'>Add</button>";
    renderDialogForm(target, html);
    document.getElementById('endptOk').onclick = fn;
    document.getElementById('endptName').focus();
}

/**
 * renders a form for updating a registrant's data
 * @param target
 * @param fn
 * @param user
 */
let renderUserForm = function(target, fn, user) {
    let html = "<div class='tm-margin' id='select-organization'></div><br>";
    html += "<div class='tm-margin' id='select-role'></div><br>";
    html += "<div class='tm-margin' id='select-ctype'></div><br>";
    html += "<input class='form-control tm-margin' type='text' size='40' id='user_lastName' placeholder='Enter Last Name'><span style='color:red;'>*</span><br>";
    html += "<input class='form-control tm-margin' type='text' size='30' id='user_firstName' placeholder='Enter First Name'><span style='color:red;'>*</span><br>";
    html += "<input class='form-control tm-margin' type='text' size='40' id='user_email' placeholder='Enter Email Address'><span style='color:red;'>*</span><br>";
    html += "<input class='form-control tm-margin' type='password' size='40' id='user_pswd' placeholder='Enter Password'><span style='color:red;'>*</span><br>";
    html += "<input class='form-control tm-margin' type='password' size='40' id='user_repswd' placeholder='Enter Re-enter Password'><span style='color:red;'>*</span><br>";
    html += "<input class='form-control tm-margin' type='text' size='40' id='user_phone' placeholder='Enter Phone Number'><br>";
    html += "<button id='registrantOk' type='button' class='btn btn-info tm-margin'>Save</button>";
    renderDialogForm(target, html);
    document.getElementById('registrantOk').onclick = fn;
    if(user.id === 0)  {
        selectOrganizations(0);
        selectRoles(0);
        selectContactTypes(0);
    } else {
//        selectOrganizations(user.organization.id);
        selectRoles(user.role);
//        selectContactTypes(user.contact.type);
        document.getElementById('user_lastName').value = user.contact.lastName;
        document.getElementById('user_firstName').value = user.contact.firstName;
        document.getElementById('user_email').value = user.contact.email;
        document.getElementById('user_phone').value = user.contact.phone;
    }
    document.getElementById('user_lastName').focus();
}

/**
 * renders a form for updating a registrant's data
 * @param target
 * @param fn
 * @param registrant
 */
let renderRegistrantForm = function(target, fn, registrant) {
    let html = "<div class='tm-margin' id='select-organization'></div><br>";
    html += "<input class='form-control tm-margin' type='text' size='40' id='detail_lastName' placeholder='Enter Last Name'><span style='color:red;'>*</span><br>";
    html += "<input class='form-control tm-margin' type='text' size='30' id='detail_firstName' placeholder='Enter First Name'><span style='color:red;'>*</span><br>";
    html += "<input class='form-control tm-margin' type='text' size='40' id='detail_email' placeholder='Enter Email Address'><span style='color:red;'>*</span><br>";
    html += "<input class='form-control tm-margin' type='text' size='40' id='detail_phone' placeholder='Enter Phone Number'><br>";
    html += "<button id='registrantOk' type='button' class='btn btn-info tm-margin'>Save</button>";
    renderDialogForm(target, html);
    document.getElementById('registrantOk').onclick = fn;
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

/**
 * renders content into a standard dialog with a close X
 * @param target
 * @param content
 */
let renderDialogForm = function(target, content)  {
    let html = "<form class='form-inline'>";
    html += "<div class='tm-form form-group'>";
    html += "<a class='tm-margin tm-right' href=\"javascript:hideIt('"+target+"');\"><span class='glyphicon glyphicon-remove'></span></a><br>";
    html += content;
    html += "</div></form>";
    document.getElementById(target).innerHTML = html;
    showIt(target);
}
