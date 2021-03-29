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

        emailService.add(MailParameter.SMTP_ADDRESS, params.smtpAddr)
        emailService.add(MailParameter.SMTP_USER, params.smtpUser)
        emailService.add(MailParameter.SMTP_HOST, params.smtpHost)
        emailService.add(MailParameter.SMTP_PORT, params.smtpPort)
        emailService.add(MailParameter.SMTP_AUTH, params.smtpAuth)
        emailService.add(MailParameter.SMTP_PSWD, params.emailPswd)

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
        if(emailService.get(MailParameter.SMTP_HOST)
           && emailService.get(MailParameter.SMTP_PORT)
           && emailService.get(MailParameter.SMTP_ADDRESS)
           && emailService.get(MailParameter.SMTP_AUTH)
           && emailService.get(MailParameter.SMTP_PSWD)
           && emailService.get(MailParameter.SMTP_USER)
        )  {
            boolean rc = emailService.sendEmail(params.attachFiles, params.emailAddr, params.emailSubject, params.emailBody)
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
