
let STATUS_HEADER = 'status-header';

/**
 * gets checked ids by class name, puts in a list and applies the function argument to them
 * @param str
 * @param fn
 */
let getCheckedIds = function(str, fn) {
    let elements = document.getElementsByClassName(str);
    let idList = "";
    for( let i=0; i < elements.length; ++i)  {
        if(elements[i].checked === true)  {
            idList += elements[i].value+":";
        }
    }
    fn(idList);
}

/**
 *  render organizations in a select control
 */
let renderSelectOrganizations = function(target, id, data)  {
    let html = "<select class='form-control' id='orgs'>";
    html += "<option value='0'>-- Select an Organization --</option>";
    data.forEach(o => {
        html += "<option value='"+o.id+"'>"+o.name+"</option>";
    });
    html += "</select><span style='color:red;'>&nbsp;&nbsp;*</span>";
    document.getElementById(target).innerHTML = html;
    document.getElementById('orgs').value = id;
}

/**
 *  render all contact types in a select control
 */
let renderContactTypes = function(target, id, data)  {
    let html = "<select id='ctypes' class='form-control'>";
    html += "<option value='0'> -- Select a Contact Type -- </option>";
    data.forEach(o => {
        html += "<option value='"+o.name+"'>"+o.name+"</option>";
    });
    html += "</select><span style='color:red;'>&nbsp;&nbsp;*</span>";
    document.getElementById(target).innerHTML = html;
    document.getElementById('ctypes').value = id;
}

/**
 *  render all contact types in a select control
 */
let renderSelectRoles = function(target, id, data)  {
    let html = "<select id='rtypes' class='form-control'>";
    html += "<option value='0'> -- Select a Role -- </option>";
    data.forEach(o => {
        html += "<option value='"+o.id+"'>"+o.authority+"</option>";
    });
    html += "</select><span style='color:red;'>&nbsp;&nbsp;*</span>";
    document.getElementById(target).innerHTML = html;
    document.getElementById('rtypes').value = id;
}

/**
 *  render all contact types in a select control
 */
let renderProviderTypes = function(target, id, data)  {
    let html = "<select id='ptypes' class='form-control'>";
    html += "<option value='0'> -- Select a Type -- </option>";
    data.forEach(o => {
        html += "<option value='"+o.name+"'>"+o.name+"</option>";
    });
    html += "</select><span style='color:red;'>&nbsp;&nbsp;*</span>";
    document.getElementById(target).innerHTML = html;
    document.getElementById('ptypes').value = id;
}

/**
 *  render the target tips for a provider endpoint
 */
let renderTargetTips = function(target, id, data)  {
    let html = "<table id='target-tip-table' class='table table-condensed table-striped table-bordered'>";
    if (data.length === 0)  {
        html += '<tr><td colspan="2"><em>There are no tips.</em></td></tr>';
    }  else {
        data.forEach(p => {
            html += "<tr>";
            html += "<td id='"+p.id+"'>" + p.conformanceTargetTipIdentifier + "</td>";
            html += "<td style='width:auto;'>";
            html += "<a href='#' onclick='deleteTargetTip(" + p.id + ");'><span class='glyphicon glyphicon-minus'></span></a>";
            html += "</td>";
            html += "</tr>";
        });
    }
    html += "</table><table class='table table-condensed table-striped table-bordered'>";
    html += "<tr>";
    html += "<td><input id='new-ttip' type='text' size='30' placeholder='Enter Partner Endpoint TIP' value=''></td>";
    html += "<td style='width:auto;'>";
    html += "<a href='#' onclick='addTargetTip("+id+");'><span class='glyphicon glyphicon-plus'></span></a>";
    html += "</td>";
    html += "</tr>";
    html += "</table>";

    document.getElementById(target).innerHTML = html;
}

//  we can write a single curry function to take an undetermined number of functions TODO
/**
 * transforms the function into 2 separate argument functions
 * @param f
 * @returns {function(*=): function(*=): *}
 */
let curryTwo = function(f)  {
    return function(a) {
        return function(b)  {
            return f(a, b);
        }
    }
}

/**
 * transforms the passed in function to 3 separate argument functions
 * @param f
 * @returns {function(*=): function(*=): function(*=): *}
 */
let curryThree = function(f)  {
    return function(a)  {
        return function(b) {
            return function(c) {
                return f(a, b, c);
            }
        }
    }
}

/**
 * transforms the passed in function to 4 separate argument functions
 * @param f
 * @returns {function(*=): function(*=): function(*=): *}
 */
let curryFour = function(f)  {
    return function(a)  {
        return function(b) {
            return function(c) {
                return function(d) {
                    return f(a, b, c, d);
                }
            }
        }
    }
}

let renderStatus = curryThree(function(target, fn, msg) {
    document.getElementById(target).innerHTML = fn(msg);
});

let setDangerStatus = renderStatus(STATUS_HEADER)(function(msg){return '<div class=\'alert alert-danger\'>'+msg+'</div>'});

let setWarningStatus = renderStatus(STATUS_HEADER)(function(msg){return '<div class=\'alert alert-warning\'>'+msg+'</div>'});

let setSuccessStatus = renderStatus(STATUS_HEADER)(function(msg){return '<div class=\'alert alert-success\'>'+msg+'</div>'});

let curriedEntries = curryFour(renderEntries);

let curriedProviderTypes = curryThree(renderProviderTypes);

let curriedContactTypes = curryThree(renderContactTypes);

let curriedSelectOrganizations = curryThree(renderSelectOrganizations);

let curriedTargetTips = curryThree(renderTargetTips);

let curriedSelectRoles = curryThree(renderSelectRoles);
