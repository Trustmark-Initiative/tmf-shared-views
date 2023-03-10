package edu.gatech.gtri.trustmark.grails.email.controller


import edu.gatech.gtri.trustmark.grails.email.service.EmailService
import grails.converters.JSON
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.multipart.MultipartFile

class EmailController {

    EmailService emailService

    def configure() {
        withFormat {
            json {
                render emailService.configure(
                        ((OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication()),
                        request.JSON.smtpAddr == null ? null : String.valueOf(request.JSON.smtpAddr),
                        request.JSON.smtpUser == null ? null : String.valueOf(request.JSON.smtpUser),
                        request.JSON.smtpHost == null ? null : String.valueOf(request.JSON.smtpHost),
                        request.JSON.smtpPort == null ? null : String.valueOf(request.JSON.smtpPort),
                        request.JSON.smtpAuth == null ? null : String.valueOf(request.JSON.smtpAuth),
                        request.JSON.emailPswd == null ? null : String.valueOf(request.JSON.emailPswd),
                        request.JSON.mailEnabled == null ? null : String.valueOf(request.JSON.mailEnabled))
                        as JSON
            }
        }
    }

    def sendEmail() {
        withFormat {
            json {
                render emailService.sendEmail(
                        ((OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication()),
                        params.attachFiles && params.attachFiles instanceof MultipartFile ? params.attachFiles : null,
                        params.emailAddr == null ? null : String.valueOf(params.emailAddr),
                        params.emailSubject == null ? null : String.valueOf(params.emailSubject),
                        params.emailBody == null ? null : String.valueOf(params.emailBody))
                        as JSON
            }
        }
    }

    def settings() {
        emailService.settings(((OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication()))
    }
}
