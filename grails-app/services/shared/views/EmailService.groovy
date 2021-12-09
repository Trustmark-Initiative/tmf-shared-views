package shared.views

import edu.gatech.gtri.trustmark.v1_0.util.TrustmarkMailClient
import grails.core.GrailsApplication
import org.springframework.web.multipart.MultipartFile

import javax.mail.util.ByteArrayDataSource

class EmailService {

    GrailsApplication grailsApplication

    def add(String name, String value) {
        TMMailParameter.save(name, value)
    }

    def delete(String name) {
        TMMailParameter.delete(name)
    }

    def update(String name, String value) {
        TMMailParameter.save(name, value)
    }

    def get(String name) {
        return TMMailParameter.find(name)
    }

    def settings() {
        log.debug("EmailService.settings ")

       if(get(TMMailParameter.SMTP_HOST) == null)  {
           log.debug("EmailService.settings is null")
           if(grailsApplication.config.smtp.from.address)  {
               add(TMMailParameter.SMTP_ADDRESS, grailsApplication.config.smtp.from.address)
           }
           if(grailsApplication.config.smtp.user) {
               add(TMMailParameter.SMTP_USER, grailsApplication.config.smtp.user)
           }
           if(grailsApplication.config.smtp.host) {
               add(TMMailParameter.SMTP_HOST, grailsApplication.config.smtp.host)
           }
           if(grailsApplication.config.smtp.port) {
               add(TMMailParameter.SMTP_PORT, grailsApplication.config.smtp.port)
           }
           if(grailsApplication.config.mail.smtp.auth) {
               add(TMMailParameter.SMTP_AUTH, grailsApplication.config.mail.smtp.auth)
           }
           if(grailsApplication.config.smtp.pswd) {
               add(TMMailParameter.SMTP_PSWD, grailsApplication.config.smtp.pswd)
           }
       }
           return [ smtpFrom: get(TMMailParameter.SMTP_ADDRESS)
                    , smtpUser: get(TMMailParameter.SMTP_USER)
                    , smtpHost: get(TMMailParameter.SMTP_HOST)
                    , smtpPort: get(TMMailParameter.SMTP_PORT)
                    , smtpAuthenticate: get(TMMailParameter.SMTP_AUTH)
                    , smtpPswd: get(TMMailParameter.SMTP_PSWD)
           ]
    }

    def sendEmail (List<MultipartFile> files, String... args)  {
        log.debug("sendEMail -> ${args[0]}")

        boolean rc = false

        TrustmarkMailClient emailClient = getMailClient()
        if(emailClient != null)  {
            if(files != null)  {
                files.forEach({f ->
                    log.debug("uploaded files ${f.originalFilename}  ${f.name}")
                    emailClient.addAttachment(f.originalFilename, new ByteArrayDataSource(f.getInputStream(), f.getContentType()))
                })
            }

            String host = get(TMMailParameter.SMTP_HOST);
            String port = get(TMMailParameter.SMTP_PORT);

            log.debug("emailClient -> host: ${host} port: ${port}")

            emailClient.setUser(get(TMMailParameter.SMTP_USER))
                    .setSmtpHost(get(TMMailParameter.SMTP_HOST))
                    .setSmtpPort(get(TMMailParameter.SMTP_PORT))
                    .setFromAddress(get(TMMailParameter.SMTP_ADDRESS))
                    .setSmtpAuthorization(Boolean.parseBoolean(get(TMMailParameter.SMTP_AUTH)))
                    .addRecipient(args[0])
                    .setSubject(args[1])
                    .setText(args[2])
                    .setPswd(get(TMMailParameter.SMTP_PSWD))

            try {
                emailClient.sendMail()
                rc = true
            } catch (Exception e)  {
                log.error(e.getMessage())
            }
        }  else {
            log.debug("TrustmarkMailClient implementation NOT FOUND!")
        }

        return rc
    }

    def sendEmailWithContent(List<MultipartFile> files, String... args)  {
        log.debug("sendEMail -> ${args[0]}")

        boolean rc = false

        TrustmarkMailClient emailClient = getMailClient()
        if(emailClient != null)  {
            if(files != null)  {
                files.forEach({f ->
                    log.debug("uploaded files ${f.originalFilename}  ${f.name}")
                    emailClient.addAttachment(f.originalFilename, new ByteArrayDataSource(f.getInputStream(), f.getContentType()))
                })
            }

            String host = get(TMMailParameter.SMTP_HOST);
            String port = get(TMMailParameter.SMTP_PORT);

            log.debug("emailClient -> host: ${host} port: ${port}")

            emailClient.setUser(get(TMMailParameter.SMTP_USER))
                    .setSmtpHost(get(TMMailParameter.SMTP_HOST))
                    .setSmtpPort(get(TMMailParameter.SMTP_PORT))
                    .setFromAddress(get(TMMailParameter.SMTP_ADDRESS))
                    .setSmtpAuthorization(Boolean.parseBoolean(get(TMMailParameter.SMTP_AUTH)))
                    .addRecipient(args[0])
                    .setSubject(args[1])
                    .setContent(args[2], "text/html")
                    .setPswd(get(TMMailParameter.SMTP_PSWD))

            try {
                emailClient.sendMail()
                rc = true
            } catch (Exception e)  {
                log.error(e.getMessage())
            }
        }  else {
            log.debug("TrustmarkMailClient implementation NOT FOUND!")
        }

        return rc
    }

    def getMailClient()  {
        ServiceLoader<TrustmarkMailClient> loader = ServiceLoader.load(TrustmarkMailClient.class);
        Iterator<TrustmarkMailClient> iterator = loader.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

}
