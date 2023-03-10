<html>
<head>
    <asset:javascript src="utility.js"/>
    <asset:javascript src="email_settings.js"/>

    <meta name="layout" content="main"/>

    <script type="text/javascript">
        initialize(
            "${createLink(controller:'email', action: 'configure')}",
            "${createLink(controller:'email', action: 'sendEmail')}")
    </script>
</head>

<body>
<div class="container pt-4">
    <h2>Email Notification</h2>

    <div class="mt-2">The email notification subsystem can be configured here to use an existing mail server for sending notifications to registered users.
    The test section allows the administrator to confirm settings.</div>

    <div id="status-header"></div>

    <div class="border rounded card mt-2">
        <div class="card-header fw-bold">
            <div class="row">
                <div class="col-11">
                    <div>Configure Email Settings</div>
                </div>
            </div>
        </div>

        <div class="card-body">

            <div class="row pb-2">
                <label class="col-2 form-check-label text-end" for="enableEmail">Enable Email</label>

                <div class="col-10">
                    <input type="checkbox" id="enableEmail" name="enableEmail"
                           class="form-check-input" ${mailEnabled ? 'checked="checked"' : ''}/>
                </div>
            </div>

            <div class="row pb-2">
                <label class="col-2 col-form-label text-end"
                       for="smtpFromAddr">SMTP From Address</label>

                <div class="col-10">
                    <input type="text" id="smtpFromAddr" name="smtpFromAddr" class="form-control"
                           value="${smtpFrom}" placeholder="Email From Address">
                </div>
            </div>

            <div class="row pb-2">
                <label class="col-2 col-form-label text-end" for="smtpUser">SMTP User Id</label>

                <div class="col-10">
                    <input type="text" id="smtpUser" name="smtpUser" class="form-control"
                           value="${smtpUser}" placeholder="SMTP User Id">
                </div>
            </div>

            <div class="row pb-2">
                <label class="col-2 col-form-label text-end" for="ePswd">SMTP Password</label>

                <div class="col-10">
                    <input type="password" id="ePswd" name="ePswd" class="form-control"
                           value="${smtpPswd}" placeholder="SMTP Password">
                </div>
            </div>

            <div class="row pb-2">
                <label class="col-2 col-form-label text-end" for="smtpHost">SMTP Host Name</label>

                <div class="col-10">
                    <input type="text" id="smtpHost" name="smtpHost" class="form-control"
                           value="${smtpHost}" placeholder="SMTP Host Name">
                </div>
            </div>

            <div class="row pb-2">
                <label class="col-2 col-form-label text-end" for="smtpPort">SMTP Port</label>

                <div class="col-10">
                    <input type="text" id="smtpPort" name="smtpPort" class="form-control"
                           value="${smtpPort}" placeholder="SMTP Port">
                </div>
            </div>

            <div class="row pb-2">
                <label class="col-2 form-check-label text-end"
                       for="smtpAuthenticate">Authenticate Mail</label>

                <div class="col-10">
                    <input type="checkbox" id="smtpAuthenticate" name="smtpAuthenticate"
                           class="form-check-input" ${smtpAuthenticate ? 'checked="checked"' : ''}/>
                </div>
            </div>
        </div>

        <div class="card-footer text-start">
            <div class="row">
                <div class="col-2"></div>

                <div class="col-10">
                    <button class="btn btn-primary" id="email-settings-update">Update</button>
                </div>
            </div>
        </div>
    </div>

    <div class="border rounded card mt-4">
        <div class="card-header fw-bold">
            <div class="row">
                <div class="col-11">
                    <div>Send Test Email</div>
                </div>
            </div>
        </div>

        <div class="card-body">
            <div class="row pb-2">
                <label class="col-2 col-form-label text-end" for="emailAddr">Email Address</label>

                <div class="col-10">
                    <input type="text" id="emailAddr" name="emailAddr" class="form-control"
                           placeholder="Email Address">
                </div>
            </div>

            <div class="row pb-2">
                <label class="col-2 col-form-label text-end" for="eSubject">Subject</label>

                <div class="col-10">
                    <input type="text" id="eSubject" name="eSubject" class="form-control"
                           placeholder="Subject">
                </div>
            </div>

            <div class="row pb-2">
                <label class="col-2 col-form-label text-end" for="emailBody">Email Text Body</label>

                <div class="col-10">
                    <textarea id="emailBody" name="emailBody" cols="30" rows="6" class="form-control"
                              placeholder="Email Text Body"></textarea>
                </div>
            </div>

            <div class="row pb-2">
                <label class="col-2 col-form-label text-end"
                       for="uploadInput">Attach Files <i>(optional)</i></label>

                <div class="col-10" id="fileUploadName">
                    <input type="file" id="uploadInput" name="fileUploadName" multiple
                           class="form-control" placeholder="Attach Files">
                </div>
            </div>

        </div>

        <div class="card-footer text-start">
            <div class="row">
                <div class="col-2"></div>

                <div class="col-10">
                    <button class="btn btn-primary" id="email-settings-send">Send</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
