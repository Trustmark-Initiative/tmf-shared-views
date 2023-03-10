package edu.gatech.gtri.trustmark.grails.oidc.domain


import org.gtri.fj.data.Option
import org.gtri.fj.function.Effect0
import org.gtri.fj.function.F0

import java.time.LocalDateTime

import static org.gtri.fj.data.List.iterableList
import static org.gtri.fj.data.Option.fromNull

// https://openid.net/specs/openid-connect-registration-1_0.html#ClientRegistration

class OidcClientRegistration {

    String requestInitialAccessToken
    String requestClientName
    String requestClientUri
    String responseClientId
    String responseClientSecret
    LocalDateTime responseClientIdIssuedAt
    LocalDateTime responseClientSecretExpiresAt
    String responseRegistrationAccessToken
    String responseRegistrationClientUri
    String responseGrantTypeList
    String responseTokenEndpointAuthMethod
    String responseScopeList
    String responseRedirectUriList
    String responseIssuerUri
    String responseJwkSetUri
    String responseTokenUri
    String responseUserInfoUri
    String responseFailureMessage

    static constraints = {
        requestClientName nullable: true
        requestClientUri nullable: true
        responseClientId nullable: true
        responseClientSecret nullable: true
        responseClientIdIssuedAt nullable: true
        responseClientSecretExpiresAt nullable: true
        responseRegistrationAccessToken nullable: true
        responseRegistrationClientUri nullable: true
        responseGrantTypeList nullable: true
        responseTokenEndpointAuthMethod nullable: true
        responseScopeList nullable: true
        responseRedirectUriList nullable: true
        responseIssuerUri nullable: true
        responseJwkSetUri nullable: true
        responseTokenUri nullable: true
        responseUserInfoUri nullable: true
        responseFailureMessage nullable: true
    }

    static mapping = {
        table "oauth2_client_registration"
        requestInitialAccessToken length: 1000
        requestClientName length: 1000
        requestClientUri length: 1000
        responseClientId length: 1000
        responseClientSecret length: 1000
        responseClientIdIssuedAt length: 1000
        responseClientSecretExpiresAt length: 1000
        responseRegistrationAccessToken length: 1000
        responseRegistrationClientUri length: 1000
        responseFailureMessage length: 1000
    }

    static final org.gtri.fj.data.List<OidcClientRegistration> findAllByOrderByRequestClientNameAscHelper() {

        fromNull(findAll(sort: 'requestClientName', order: 'asc'))
                .map({ collection -> iterableList((Iterable<OidcClientRegistration>) collection) })
                .orSome(org.gtri.fj.data.List.<OidcClientRegistration> nil());
    }

    static final org.gtri.fj.data.Option<OidcClientRegistration> findByRequestClientNameHelper(final String clientName) {
        fromNull(findByRequestClientName(clientName))
    }

    static final org.gtri.fj.data.Option<OidcClientRegistration> findByRequestClientUriHelper(final String clientUri) {
        fromNull(findByRequestClientUri(clientUri))
    }

    static final org.gtri.fj.data.Option<OidcClientRegistration> findByUriTemplateHelper(final String uriTemplate) {
        fromNull(OidcClientRegistration.executeQuery("SELECT oidcClientRegistration FROM OidcClientRegistration oidcClientRegistration WHERE '" + uriTemplate + "' LIKE CONCAT(oidcClientRegistration.requestClientUri, '%')"))
                .map({ collection -> iterableList((Iterable<OidcClientRegistration>) collection) })
                .orSome(org.gtri.fj.data.List.<OidcClientRegistration> nil())
                .headOption()
    }

    static final org.gtri.fj.data.Option<OidcClientRegistration> findByResponseClientIdHelper(final String clientName) {
        fromNull(findByResponseClientId(clientName))
    }

    Long idHelper() {
        id
    }

    void deleteHelper() {
        delete(failOnError: true);
    }

    void deleteAndFlushHelper() {
        delete(flush: true, failOnError: true)
    }

    OidcClientRegistration saveHelper() {
        save(failOnError: true)
    }

    OidcClientRegistration saveAndFlushHelper() {
        save(flush: true, failOnError: true)
    }

    static final <T> T withTransactionHelper(final F0<T> f0) {
        return withTransaction({ return f0.f() })
    }

    static final void withTransactionHelper(final Effect0 effect0) {
        withTransaction({ return effect0.f() })
    }

    static Option<OidcClientRegistration> findByIdHelper(final long id) {
        fromNull(findById(id))
    }

    static org.gtri.fj.data.List<OidcClientRegistration> findAllHelper() {
        fromNull(findAll())
                .map({ collection -> iterableList(collection) })
                .orSome(org.gtri.fj.data.List.<OidcClientRegistration> nil());
    }
}
