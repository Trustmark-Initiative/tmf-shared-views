function initialize(
    emailConfigureUrl,
    emailSendUrl) {

    document.addEventListener("readystatechange", function () {

        if (document.readyState === "complete") {
            onComplete()
        }
    })

    function onComplete() {

        document.getElementById("email-settings-update").addEventListener("click",
            () => emailSettings(
                document.getElementById("smtpFromAddr").value,
                document.getElementById("smtpUser").value,
                document.getElementById("smtpHost").value,
                document.getElementById("smtpPort").value,
                document.getElementById("smtpAuthenticate").checked,
                document.getElementById("ePswd").value,
                document.getElementById("enableEmail").checked));

        document.getElementById("email-settings-send").addEventListener("click",
            () => sendEmail(
                document.getElementById("emailAddr").value,
                document.getElementById("eSubject").value,
                document.getElementById("emailBody").value,
                document.getElementById("uploadInput")));

        /**
         * Calls the email controller saving the settings in the db
         */
        let emailSettings = function (addr, user, host, port, auth, pswd, mailEnabled) {

            let onFailure = function (response) {
            }
            let onSuccess = function (jsonPromise) {
                jsonPromise.then(json => {
                    if (json.rc === "success") {
                        document.getElementById("status-header").innerHTML = `<div class="alert alert-success" style="margin-top: 2em;">${json.message}</div>`;
                    } else {
                        document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">${json.message}</div>`;
                    }
                });
            }

            if (!checkEmailSettings(addr, user, host, port, auth, pswd, mailEnabled)) {
                return;
            }
            fetchPost(emailConfigureUrl, {
                smtpAddr: addr,
                smtpUser: user,
                smtpHost: host,
                smtpPort: port,
                smtpAuth: auth,
                emailPswd: pswd,
                mailEnabled, mailEnabled,
                timestamp: new Date().getTime(),
            })
                .then(response => response.status !== 200 ?
                    onFailure(response.json()) :
                    onSuccess(response.json()))
        }

        /**
         * Calls the appearance/adminPswd method remotely, gets the JSON, and displays success failure.
         */
        let sendEmail = function (addr, subj, txtBody, uploads) {

            let onFailure = function (response) {
            }
            let onSuccess = function (jsonPromise) {
                jsonPromise.then(json => {
                    if (json.rc === "success") {
                        document.getElementById("status-header").innerHTML = `<div class="alert alert-success" style="margin-top: 2em;">${json.message}</div>`;
                    } else {
                        document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">${json.message}</div>`;
                    }
                });
            }

            if (!checkSendMailForm(addr, subj, txtBody)) {
                return;
            }
            let fd = new FormData();
            for (let i = 0; i < uploads.files.length; ++i) {
                fd.append("attachFiles", uploads.files[i]);
            }
            fd.append("emailAddr", addr);
            fd.append("emailSubject", subj);
            fd.append("emailBody", txtBody);
            fd.append("timestamp", new Date().getTime());
            fd.append("format", "JSON");

            fetchUpload(emailSendUrl, fd)
                .then(response => response.status !== 200 ?
                    onFailure(response.json()) :
                    onSuccess(response.json()))
        }
        /**
         * check the settings form for errors
         *
         */
        let checkEmailSettings = function (addr, user, host, port, auth, pswd) {
            if (addr == null || addr === "") {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">From Address cannot be blank.</div>`;
                return false;
            }
            if (!addr.includes("@")) {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">From Address is invalid.</div>`;
                return false;
            }
            if (user == null || user === "") {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">User cannot be blank.</div>`;
                return false;
            }
            if (host == null || host === "") {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">SMTP Host cannot be blank.</div>`;
                return false;
            }
            if (port == null || port === "") {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">SMTP Port cannot be blank.</div>`;
                return false;
            }
            let portno = parseInt(port);
            if (!Number.isInteger(portno)) {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">SMTP Port must be an integer.</div>`;
                return false;
            }
            if (pswd == null || pswd === "") {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">SMTP Password cannot be blank.</div>`;
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
        let checkSendMailForm = function (addr, subj, txtBody) {
            if (addr == null || addr === "") {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">Address cannot be blank.</div>`;
                return false;
            }
            if (subj == null || subj === "") {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-warning" style="margin-top: 2em;">Subject is blank.</div>`;
                return false;
            }
            if (txtBody == null || txtBody === "") {
                document.getElementById("status-header").innerHTML = `<div class="alert alert-danger" style="margin-top: 2em;">Email has no body.</div>`;
                return false;
            }
            return true;
        }
    }
}
