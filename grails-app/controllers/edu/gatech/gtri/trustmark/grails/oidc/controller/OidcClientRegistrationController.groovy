package edu.gatech.gtri.trustmark.grails.oidc.controller

import edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationRequestDeleteAll
import edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationRequestFindAll
import edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationRequestFindOne
import edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationRequestInsert
import edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationRequestUpdate
import edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationService
import edu.gatech.gtri.trustmark.v1_0.web.validation.ValidationResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken

import static edu.gatech.gtri.trustmark.v1_0.web.validation.ValidationResponseUtility.validationResponse

class OidcClientRegistrationController {

    OidcClientRegistrationService oidcClientRegistrationService

    Object manage() {
    }

    Object findAll(final OidcClientRegistrationRequestFindAll oidcClientRegistrationRequestFindAll) {

        ValidationResponse validationResponse = validationResponse(
                oidcClientRegistrationService
                        .findAll(
                                (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication(),
                                oidcClientRegistrationRequestFindAll)
                        .map(list -> list.toJavaList()))

        respond validationResponse.getResponseBody(), status: validationResponse.getResponseStatus()
    }

    Object findOne(final OidcClientRegistrationRequestFindOne oidcClientRegistrationRequestFindOne) {

        ValidationResponse validationResponse = validationResponse(
                oidcClientRegistrationService
                        .findOne(
                                (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication(),
                                oidcClientRegistrationRequestFindOne))

        respond validationResponse.getResponseBody(), status: validationResponse.getResponseStatus()
    }

    Object insert(final OidcClientRegistrationRequestInsert oidcClientRegistrationRequestInsert) {

        ValidationResponse validationResponse = validationResponse(
                oidcClientRegistrationService
                        .insert(
                                (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication(),
                                oidcClientRegistrationRequestInsert))

        respond validationResponse.getResponseBody(), status: validationResponse.getResponseStatus()
    }

    Object update(final OidcClientRegistrationRequestUpdate oidcClientRegistrationRequestUpdate) {

        ValidationResponse validationResponse = validationResponse(
                oidcClientRegistrationService
                        .update(
                                (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication(),
                                oidcClientRegistrationRequestUpdate))

        respond validationResponse.getResponseBody(), status: validationResponse.getResponseStatus()
    }

    Object delete(final OidcClientRegistrationRequestDeleteAll oidcClientRegistrationRequestDeleteAll) {

        ValidationResponse validationResponse = validationResponse(
                oidcClientRegistrationService
                        .delete(
                                (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication(),
                                oidcClientRegistrationRequestDeleteAll))

        respond validationResponse.getResponseBody(), status: validationResponse.getResponseStatus()
    }
}
