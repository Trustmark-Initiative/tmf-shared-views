package shared.views

import grails.converters.JSON

import org.springframework.web.multipart.MultipartFile

import javax.mail.util.ByteArrayDataSource

class EmailController {

    EmailService emailService

    def index() { }

    /**
     *
     * @return
     */
    def configure()  {
        log.debug("EmailController.configure ${params.smtpAddr} ${params.smtpHost} ${params.smtpUser} ${params.smtpAuth} ${params.emailPswd}")

        def status = [rc: "success", message: "Success updated email settings."]

        emailService.add(TMMailParameter.SMTP_ADDRESS, params.smtpAddr)
        emailService.add(TMMailParameter.SMTP_USER, params.smtpUser)
        emailService.add(TMMailParameter.SMTP_HOST, params.smtpHost)
        emailService.add(TMMailParameter.SMTP_PORT, params.smtpPort)
        emailService.add(TMMailParameter.SMTP_AUTH, params.smtpAuth)
        emailService.add(TMMailParameter.SMTP_PSWD, params.emailPswd)
        emailService.add(TMMailParameter.MAIL_ENABLED, params.mailEnabled)

        withFormat  {
            json {
                render status as JSON
            }
        }
    }

    /**
     *
     * @return
     */
    def settings ()  {
        log.debug("EmailController.settings ...")

        return emailService.settings()
    }

    /**
     *
     * @return
     */
    def sendEmail ()  {
        log.debug("EmailController.sendEmail ... ${params.emailAddr} ")

        def status = [rc: "success", message: "Successfully sent email."]
        if(emailService.get(TMMailParameter.SMTP_HOST)
           && emailService.get(TMMailParameter.SMTP_PORT)
           && emailService.get(TMMailParameter.SMTP_ADDRESS)
           && emailService.get(TMMailParameter.SMTP_AUTH)
           && emailService.get(TMMailParameter.SMTP_PSWD)
           && emailService.get(TMMailParameter.SMTP_USER)
        )  {

            boolean rc = false

            if (params.attachFiles && params.attachFiles instanceof MultipartFile) {
                List<MultipartFile> attachedFiles = new ArrayList<>()
                attachedFiles.add(params.attachFiles)
                rc = emailService.sendEmail(attachedFiles, params.emailAddr, params.emailSubject, params.emailBody)
            } else {
                rc = emailService.sendEmail(params.attachFiles, params.emailAddr, params.emailSubject, params.emailBody)
            }

            if(!rc)  {
                status.rc = "failure"
                status.message = "Email not sent!"
            }
        }  else  {
            status.rc = "failure"
            status.message = "Email Parameters are not set!"
        }

        withFormat  {
            json {
                render status as JSON
            }
        }
    }
}
