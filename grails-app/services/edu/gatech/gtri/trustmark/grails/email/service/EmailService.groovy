package edu.gatech.gtri.trustmark.grails.email.service

import edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter
import edu.gatech.gtri.trustmark.v1_0.util.TrustmarkMailClient
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.multipart.MultipartFile

import javax.mail.util.ByteArrayDataSource

import static edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter.MAIL_ENABLED
import static edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter.SMTP_ADDRESS
import static edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter.SMTP_AUTH
import static edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter.SMTP_HOST
import static edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter.SMTP_PORT
import static edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter.SMTP_PSWD
import static edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter.SMTP_USER
import static edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter.find
import static edu.gatech.gtri.trustmark.grails.email.domain.EmailParameter.save

@Transactional
class EmailService {

    GrailsApplication grailsApplication

    def configure(
            OAuth2AuthenticationToken oAuth2AuthenticationToken,
            String smtpAddr,
            String smtpUser,
            String smtpHost,
            String smtpPort,
            String smtpAuth,
            String emailPswd,
            String mailEnabled) {

        if (oAuth2AuthenticationToken.authorities.contains(new SimpleGrantedAuthority("tf-email-admin"))) {

            add(EmailParameter.SMTP_ADDRESS, smtpAddr)
            add(EmailParameter.SMTP_USER, smtpUser)
            add(EmailParameter.SMTP_HOST, smtpHost)
            add(EmailParameter.SMTP_PORT, smtpPort)
            add(EmailParameter.SMTP_AUTH, smtpAuth)
            add(EmailParameter.SMTP_PSWD, emailPswd)
            add(EmailParameter.MAIL_ENABLED, mailEnabled)

            [rc: "success", message: "Success updated email settings."]

        } else {
            throw new AccessDeniedException("tf-oidc-admin")
        }
    }

    def sendEmail(
            OAuth2AuthenticationToken oAuth2AuthenticationToken,
            MultipartFile attachFiles,
            String emailAddr,
            String emailSubject,
            String emailBody) {

        if (oAuth2AuthenticationToken.authorities.contains(new SimpleGrantedAuthority("tf-email-admin"))) {

            def status = [rc: "success", message: "Successfully sent email."]

            if (get(EmailParameter.SMTP_HOST)
                    && get(EmailParameter.SMTP_PORT)
                    && get(EmailParameter.SMTP_ADDRESS)
                    && get(EmailParameter.SMTP_AUTH)
                    && get(EmailParameter.SMTP_PSWD)
                    && get(EmailParameter.SMTP_USER)) {

                boolean rc = false

                if (attachFiles == null) {
                    rc = sendEmailHelper(attachFiles, emailAddr, emailSubject, emailBody)
                } else {
                    List<MultipartFile> attachedFiles = new ArrayList<>()
                    attachedFiles.add(attachFiles)
                    rc = sendEmailHelper(attachedFiles, emailAddr, emailSubject, emailBody)
                }

                if (!rc) {
                    status.rc = "failure"
                    status.message = "Email not sent!"
                }
            } else {
                status.rc = "failure"
                status.message = "Email Parameters are not set!"
            }

            status

        } else {
            throw new AccessDeniedException("tf-oidc-admin")
        }
    }

    def settings(OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        if (oAuth2AuthenticationToken.authorities.contains(new SimpleGrantedAuthority("tf-email-admin"))) {

            if (get(SMTP_HOST) == null) {
                if (grailsApplication.config.smtp.from.address) {
                    add(SMTP_ADDRESS, grailsApplication.config.smtp.from.address)
                }
                if (grailsApplication.config.smtp.user) {
                    add(SMTP_USER, grailsApplication.config.smtp.user)
                }
                if (grailsApplication.config.smtp.host) {
                    add(SMTP_HOST, grailsApplication.config.smtp.host)
                }
                if (grailsApplication.config.smtp.port) {
                    add(SMTP_PORT, grailsApplication.config.smtp.port)
                }
                if (grailsApplication.config.mail.smtp.auth) {
                    add(SMTP_AUTH, grailsApplication.config.mail.smtp.auth)
                }
                if (grailsApplication.config.smtp.pswd) {
                    add(SMTP_PSWD, grailsApplication.config.smtp.pswd)
                }
                if (grailsApplication.config.mail.enabled) {
                    add(MAIL_ENABLED, grailsApplication.config.mail.enabled)
                }
            }
            return [
                    smtpFrom        : get(SMTP_ADDRESS),
                    smtpUser        : get(SMTP_USER),
                    smtpHost        : get(SMTP_HOST),
                    smtpPort        : get(SMTP_PORT),
                    smtpAuthenticate: Boolean.parseBoolean(get(SMTP_AUTH)),
                    smtpPswd        : get(SMTP_PSWD),
                    mailEnabled     : Boolean.parseBoolean(get(MAIL_ENABLED))]

        } else {
            throw new AccessDeniedException("tf-oidc-admin")
        }
    }

    private def add(String name, String value) {
        save(name, value)
    }

    private def delete(String name) {
        EmailParameter.delete(name)
    }

    private def update(String name, String value) {
        save(name, value)
    }

    private def get(String name) {
        return find(name)
    }

    private def sendEmailHelper(List<MultipartFile> files, String... args) {
        log.debug("sendEMail -> ${args[0]}")

        boolean rc = false

        TrustmarkMailClient emailClient = getMailClient()
        if (emailClient != null) {
            if (files != null) {
                files.forEach({ f ->
                    log.debug("uploaded files ${f.originalFilename}  ${f.name}")
                    emailClient.addAttachment(f.originalFilename, new ByteArrayDataSource(f.getInputStream(), f.getContentType()))
                })
            }

            String host = get(SMTP_HOST);
            String port = get(SMTP_PORT);

            log.debug("emailClient -> host: ${host} port: ${port}")

            emailClient.setUser(get(SMTP_USER))
                    .setSmtpHost(get(SMTP_HOST))
                    .setSmtpPort(get(SMTP_PORT))
                    .setFromAddress(get(SMTP_ADDRESS))
                    .setSmtpAuthorization(Boolean.parseBoolean(get(SMTP_AUTH)))
                    .addRecipient(args[0])
                    .setSubject(args[1])
                    .setText(args[2])
                    .setPswd(get(SMTP_PSWD))

            try {
                emailClient.sendMail()
                rc = true
            } catch (Exception e) {
                log.error(e.getMessage())
            }
        } else {
            log.debug("TrustmarkMailClient implementation NOT FOUND!")
        }

        return rc
    }

    private def sendEmailWithContent(List<MultipartFile> files, String... args) {
        log.debug("sendEMail -> ${args[0]}")

        boolean rc = false

        TrustmarkMailClient emailClient = getMailClient()
        if (emailClient != null) {
            if (files != null) {
                files.forEach({ f ->
                    log.debug("uploaded files ${f.originalFilename}  ${f.name}")
                    emailClient.addAttachment(f.originalFilename, new ByteArrayDataSource(f.getInputStream(), f.getContentType()))
                })
            }

            String host = get(SMTP_HOST);
            String port = get(SMTP_PORT);

            log.debug("emailClient -> host: ${host} port: ${port}")

            emailClient.setUser(get(SMTP_USER))
                    .setSmtpHost(get(SMTP_HOST))
                    .setSmtpPort(get(SMTP_PORT))
                    .setFromAddress(get(SMTP_ADDRESS))
                    .setSmtpAuthorization(Boolean.parseBoolean(get(SMTP_AUTH)))
                    .addRecipient(args[0])
                    .setSubject(args[1])
                    .setContent(args[2], "text/html")
                    .setPswd(get(SMTP_PSWD))

            try {
                emailClient.sendMail()
                rc = true
            } catch (Exception e) {
                log.error(e.getMessage())
            }
        } else {
            log.debug("TrustmarkMailClient implementation NOT FOUND!")
        }

        return rc
    }

    private def getMailClient() {
        ServiceLoader<TrustmarkMailClient> loader = ServiceLoader.load(TrustmarkMailClient.class);
        Iterator<TrustmarkMailClient> iterator = loader.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    private def mailEnabled() {
        if (get(MAIL_ENABLED)) {
            return Boolean.parseBoolean(get(MAIL_ENABLED))
        } else if (grailsApplication.config.mail.enabled) {
            return Boolean.parseBoolean(grailsApplication.config.mail.enabled)
        }

        return false
    }
}
