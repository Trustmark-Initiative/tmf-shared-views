<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main"/>
    <title>Configure Email</title>
<script type="text/javascript">
    /**
     * Calls the email controller saving the settings in the db
     */
    let emailSettings = function(addr, user, host, port, auth, pswd) {
        console.log("emailSettings -> "+addr+","+host+","+auth);
        if(!checkEmailSettings(addr, user, host, port, auth, pswd)) {
            return;
        }
        $.ajax({
            url: '${createLink(controller: 'email', action: 'configure')}',
            method: 'POST',
            dataType: 'json',
            data: {
                smtpAddr: addr,
                smtpUser: user,
                smtpHost: host,
                smtpPort: port,
                smtpAuth: auth,
                emailPswd: pswd,
                timestamp: new Date().getTime(),
                format: "JSON"
            },
            success: function(data, textStatus, jqXHR) {
                console.log("Email set");
                if(data.rc === 'success')  {
                    $('#status-header').html('<div class="alert alert-success" style="margin-top: 2em;">'+data.message+'</div>');
                }  else  {
                    $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">'+data.message+'</div>');
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("Error configuring email! "+textStatus);
            }
        })
    }

    /**
     * Calls the appearance/adminPswd method remotely, gets the JSON, and displays success failure.
     */
    let sendEmail = function(addr, subj, txtBody, uploads) {
        console.log("sendEmail -> "+addr +","+subj+","+txtBody+","+uploads.files[0]);

        if(!checkSendMailForm(addr, subj, txtBody))  {
            return;
        }
        let fd = new FormData();
        for ( let i=0; i < uploads.files.length;++i) {
            fd.append('attachFiles', uploads.files[i]);
        }
        fd.append('emailAddr', addr);
        fd.append('emailSubject', subj);
        fd.append('emailBody', txtBody);
        fd.append('timestamp', new Date().getTime());
        fd.append('format', "JSON");

        $.ajax({
            url: '${createLink(controller: 'email', action: 'sendEmail')}',
            method: 'POST',
            processData: false,
            contentType: false,
            data: fd,
            success: function(data, textStatus, jqXHR) {
                console.log("Email sent");
                if(data.rc === 'success')  {
                    $('#status-header').html('<div class="alert alert-success" style="margin-top: 2em;">'+data.message+'</div>');
                }  else  {
                    $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">'+data.message+'</div>');
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("Error sending emails! "+ textStatus);
            }
        })
    }
    /**
     * check the settings form for errors
     *
     */
    let checkEmailSettings = function(addr, user, host, port, auth, pswd) {
        if(addr == null || addr === "")  {
            $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">From Address cannot be blank.</div>');
            return false;
        }
        if(!addr.includes("@"))  {
            $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">From Address is invalid.</div>');
            return false;
        }
        if(user == null || user === "")  {
            $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">User cannot be blank.</div>');
            return false;
        }
        if(host == null || host === "")  {
            $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">SMTP Host cannot be blank.</div>');
            return false;
        }
        if(port == null || port === "")  {
            $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">SMTP Port cannot be blank.</div>');
            return false;
        }
        let portno = parseInt(port);
        if(!Number.isInteger(portno))  {
            $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">SMTP Port must be an integer.</div>');
            return false;
        }
        if(pswd == null || pswd === "")  {
            $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">SMTP Password cannot be blank.</div>');
            return false;
        }
        return true;
    }


    /**
     * check the sendmail Form for errors
     * @param addr
     * @param subj
     * @param txtBody
     * @returns {boolean}
     */
    let checkSendMailForm = function(addr, subj, txtBody)  {
        if(addr == null || addr === "")  {
            $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">Address cannot be blank.</div>');
            return false;
        }
        if(subj == null || subj === "")  {
            $('#status-header').html('<div class="alert alert-warning" style="margin-top: 2em;">Subject is blank.</div>');
            return false;
        }
        if(txtBody == null || txtBody === "")  {
            $('#status-header').html('<div class="alert alert-danger" style="margin-top: 2em;">Email has no body.</div>');
            return false;
        }
        return true;
    }
</script>
</head>
<body>
<h2>Email Notification</h2>
<p>The email notification subsystem can be configured here to use an existing mail server for sending notifications to registered users.
The test section allows the administrator to confirm settings.</p>
<div id="status-header"></div>
<div class="row">
    <div class="col-lg-6" style="margin-top: 6em;">
        <h3>Configure Email Settings</h3>
        <div class="form-group">
            <div class="row">
                <div class="col-md-6">SMTP From Address:<input id="smtpFromAddr" type="text" class="form-control" value="${smtpFrom}" placeholder="Email From Address" /></div>
            </div>
            <div class="row">
                <div class="col-md-6">SMTP User Id:<input id="smtpUser" type="text" class="form-control" value="${smtpUser}" placeholder="SMTP User Id" /></div>
            </div>
            <div class="row">
                <div class="col-md-6">SMTP Password:<input id="ePswd" type="password" class="form-control" value="${smtpPswd}" placeholder="SMTP Password" /></div>
            </div>
            <div class="row">
                <div class="col-md-6">SMTP Host Name:<input id="smtpHost" type="text" class="form-control" value="${smtpHost}" placeholder="SMTP Host Name" /></div>
            </div>
            <div class="row">
                <div class="col-md-6">SMTP Port:<input id="smtpPort" type="text" class="form-control" value="${smtpPort}" placeholder="SMTP Port" /></div>
            </div>
            <div class="row">
                <div class="col-md-6">Authenticate Mail?&nbsp;<input id="smtpAuthenticate" type="checkbox"/></div>
            </div>
            <div class="row">
                <div class="col-md-6" style="text-align: center;">
                    <button onclick="emailSettings(document.getElementById('smtpFromAddr').value
                        , document.getElementById('smtpUser').value
                        , document.getElementById('smtpHost').value
                        , document.getElementById('smtpPort').value
                        , document.getElementById('smtpAuthenticate').checked
                        , document.getElementById('ePswd').value);" class="btn btn-primary">Update</button>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-6" style="margin-top: 6em;">
        <h3>Send Test Email</h3>
        <div class="form-group">
            <div class="row">
                <div class="col-md-6"><input id="emailAddr" type="text" class="form-control" placeholder="Email Address" /></div>
            </div>
            <div class="row">
                <div class="col-md-6"><input id="eSubject" type="text" class="form-control" placeholder="Subject" /></div>
            </div>
            <div class="row">
                <div class="col-md-6"><textarea id="emailBody" cols="30" rows="6" class="form-control" placeholder="Email Text Body"></textarea></div>
            </div>
            <div class="row">
                <h5>Attach Files <i>(optional)</i></h5>
                <div class="col-md-6" id="fileUploadName">
                    <input type="file" id="uploadInput" multiple>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6" style="text-align: center;">
                    <button onclick="sendEmail(document.getElementById('emailAddr').value
                        , document.getElementById('eSubject').value
                        , document.getElementById('emailBody').value
                        , document.getElementById('uploadInput'))" class="btn btn-primary">Send</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>