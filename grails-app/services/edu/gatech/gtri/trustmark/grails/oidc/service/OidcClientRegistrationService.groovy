package edu.gatech.gtri.trustmark.grails.oidc.service

import edu.gatech.gtri.trustmark.grails.oidc.domain.OidcClientRegistration
import edu.gatech.gtri.trustmark.v1_0.FactoryLoader
import edu.gatech.gtri.trustmark.v1_0.oidc.OidcClientRegistrar
import edu.gatech.gtri.trustmark.v1_0.oidc.OidcClientRegistrationException
import edu.gatech.gtri.trustmark.v1_0.web.validation.ValidationMessage
import grails.gorm.transactions.Transactional
import org.gtri.fj.data.List
import org.gtri.fj.data.NonEmptyList
import org.gtri.fj.data.Validation
import org.gtri.fj.product.Unit
import org.json.JSONArray
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken

import java.time.ZoneOffset

import static edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationUtility.oidcClientRegistrationResponse
import static edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationUtility.validationId
import static edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationUtility.validationIdEditable
import static edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationUtility.validationIdList
import static edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationUtility.validationRequestClientName
import static edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationUtility.validationRequestClientUri
import static edu.gatech.gtri.trustmark.grails.oidc.service.OidcClientRegistrationUtility.validationRequestInitialAccessToken
import static edu.gatech.gtri.trustmark.v1_0.web.validation.ValidationUtility.requesterMay
import static org.gtri.fj.data.List.iterableList
import static org.gtri.fj.data.Validation.accumulate
import static org.gtri.fj.data.Validation.success
import static org.gtri.fj.product.Unit.unit
import static org.springframework.security.core.authority.AuthorityUtils.authorityListToSet

@Transactional
class OidcClientRegistrationService {

    public Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, List<OidcClientRegistrationResponse>> findAll(
            final OAuth2AuthenticationToken oAuth2AuthenticationToken,
            final OidcClientRegistrationRequestFindAll oidcClientRegistrationRequestFindAll) {

        return requesterMay(
                oAuth2AuthenticationToken,
                oAuth2AuthenticationTokenInner -> authorityListToSet(oAuth2AuthenticationTokenInner.getAuthorities()),
                "tf-oidc-administrator",
                oAuth2AuthenticationTokenInner -> findAllHelper(oAuth2AuthenticationTokenInner, oidcClientRegistrationRequestFindAll));
    }

    public Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, OidcClientRegistrationResponse> findOne(
            final OAuth2AuthenticationToken oAuth2AuthenticationToken,
            final OidcClientRegistrationRequestFindOne oidcClientRegistrationRequestFindOne) {

        return requesterMay(
                oAuth2AuthenticationToken,
                oAuth2AuthenticationTokenInner -> authorityListToSet(oAuth2AuthenticationTokenInner.getAuthorities()),
                "tf-oidc-administrator",
                oAuth2AuthenticationTokenInner -> findOneHelper(oAuth2AuthenticationTokenInner, oidcClientRegistrationRequestFindOne));
    }

    public Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, OidcClientRegistrationResponse> insert(
            final OAuth2AuthenticationToken oAuth2AuthenticationToken,
            final OidcClientRegistrationRequestInsert oidcClientRegistrationRequestInsert) {

        return requesterMay(
                oAuth2AuthenticationToken,
                oAuth2AuthenticationTokenInner -> authorityListToSet(oAuth2AuthenticationTokenInner.getAuthorities()),
                "tf-oidc-administrator",
                oAuth2AuthenticationTokenInner -> insertHelper(oAuth2AuthenticationTokenInner, oidcClientRegistrationRequestInsert));
    }

    public Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, OidcClientRegistrationResponse> update(
            final OAuth2AuthenticationToken oAuth2AuthenticationToken,
            final OidcClientRegistrationRequestUpdate oidcClientRegistrationRequestUpdate) {

        return requesterMay(
                oAuth2AuthenticationToken,
                oAuth2AuthenticationTokenInner -> authorityListToSet(oAuth2AuthenticationTokenInner.getAuthorities()),
                "tf-oidc-administrator",
                oAuth2AuthenticationTokenInner -> updateHelper(oAuth2AuthenticationTokenInner, oidcClientRegistrationRequestUpdate));
    }

    private Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, List<OidcClientRegistrationResponse>> findAllHelper(
            final OAuth2AuthenticationToken oAuth2AuthenticationToken,
            final OidcClientRegistrationRequestFindAll oidcClientRegistrationRequestFindAll) {

        return success(OidcClientRegistration
                .findAllByOrderByRequestClientNameAscHelper()
                .map(oidcClientRegistration -> oidcClientRegistrationResponse(oidcClientRegistration, oidcClientRegistration.getResponseClientId() == null || oidcClientRegistration.getResponseClientId().isEmpty())));
    }

    private Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, OidcClientRegistrationResponse> findOneHelper(
            final OAuth2AuthenticationToken oAuth2AuthenticationToken,
            final OidcClientRegistrationRequestFindOne oidcClientRegistrationRequestFindOne) {

        return validationId(oidcClientRegistrationRequestFindOne.getId())
                .map(oidcClientRegistration -> oidcClientRegistrationResponse(oidcClientRegistration, oidcClientRegistration.getResponseClientId() == null || oidcClientRegistration.getResponseClientId().isEmpty()));
    }

    private Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, OidcClientRegistrationResponse> insertHelper(
            final OAuth2AuthenticationToken oAuth2AuthenticationToken,
            final OidcClientRegistrationRequestInsert oidcClientRegistrationRequestInsert) {

        return accumulate(
                success(new OidcClientRegistration()),
                validationRequestInitialAccessToken(oidcClientRegistrationRequestInsert.getRequestInitialAccessToken()),
                validationRequestClientName(oidcClientRegistrationRequestInsert.getRequestClientName()),
                validationRequestClientUri(oidcClientRegistrationRequestInsert.getRequestClientUri()),
                this::saveHelper)
                .map(this::synchronizeHelper);
    }

    private Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, OidcClientRegistrationResponse> updateHelper(
            final OAuth2AuthenticationToken oAuth2AuthenticationToken,
            final OidcClientRegistrationRequestUpdate oidcClientRegistrationRequestUpdate) {

        return accumulate(
                validationIdEditable(oidcClientRegistrationRequestUpdate.getId()),
                validationRequestInitialAccessToken(oidcClientRegistrationRequestUpdate.getRequestInitialAccessToken()),
                validationRequestClientName(oidcClientRegistrationRequestUpdate.getId(), oidcClientRegistrationRequestUpdate.getRequestClientName()),
                validationRequestClientUri(oidcClientRegistrationRequestUpdate.getId(), oidcClientRegistrationRequestUpdate.getRequestClientUri()),
                this::saveHelper)
                .map(this::synchronizeHelper);
    }

    private OidcClientRegistration saveHelper(
            final OidcClientRegistration oidcClientRegistration,
            final String requestInitialAccessToken,
            final String requestClientName,
            final String requestClientUri) {

        oidcClientRegistration.setRequestInitialAccessToken(requestInitialAccessToken);
        oidcClientRegistration.setRequestClientName(requestClientName);
        oidcClientRegistration.setRequestClientUri(requestClientUri);

        return oidcClientRegistration.saveAndFlushHelper();
    }

    private OidcClientRegistrationResponse synchronizeHelper(
            final OidcClientRegistration oidcClientRegistrationDomain) {

        final OidcClientRegistrar oidcClientRegistrar = FactoryLoader.getInstance(OidcClientRegistrar.class);

        try {
            edu.gatech.gtri.trustmark.v1_0.oidc.OidcClientRegistration oidcClientRegistration = oidcClientRegistrar.register(
                    oidcClientRegistrationDomain.getRequestInitialAccessToken(),
                    oidcClientRegistrationDomain.getRequestClientName(),
                    oidcClientRegistrationDomain.getRequestClientUri());

            oidcClientRegistrationDomain.setResponseClientId(oidcClientRegistration.getClientId());
            oidcClientRegistrationDomain.setResponseClientSecret(oidcClientRegistration.getClientSecret());
            oidcClientRegistrationDomain.setResponseClientIdIssuedAt(oidcClientRegistration.getClientIdIssuedAt().atOffset(ZoneOffset.UTC).toLocalDateTime());
            oidcClientRegistrationDomain.setResponseClientSecretExpiresAt(oidcClientRegistration.getClientSecretExpiresAt().map(instant -> instant.atOffset(ZoneOffset.UTC).toLocalDateTime()).toNull());
            oidcClientRegistrationDomain.setResponseRegistrationAccessToken(oidcClientRegistration.getRegistrationAccessToken());
            oidcClientRegistrationDomain.setResponseRegistrationClientUri(oidcClientRegistration.getRegistrationClientUri());
            oidcClientRegistrationDomain.setResponseGrantTypeList(new JSONArray(oidcClientRegistration.getGrantTypeList()).toString());
            oidcClientRegistrationDomain.setResponseTokenEndpointAuthMethod(oidcClientRegistration.getTokenEndpointAuthMethod());
            oidcClientRegistrationDomain.setResponseScopeList(new JSONArray(oidcClientRegistration.getScopeList()).toString());
            oidcClientRegistrationDomain.setResponseRedirectUriList(new JSONArray(oidcClientRegistration.getRedirectUriList()).toString());
            oidcClientRegistrationDomain.setResponseIssuerUri(oidcClientRegistration.getIssuerUri());
            oidcClientRegistrationDomain.setResponseJwkSetUri(oidcClientRegistration.getJwkSetUri());
            oidcClientRegistrationDomain.setResponseTokenUri(oidcClientRegistration.getTokenUri());
            oidcClientRegistrationDomain.setResponseUserInfoUri(oidcClientRegistration.getUserInfoUri());
            oidcClientRegistrationDomain.setResponseFailureMessage(null);

        } catch (final OidcClientRegistrationException oidcClientRegistrationException) {

            oidcClientRegistrationDomain.setResponseClientId(null);
            oidcClientRegistrationDomain.setResponseClientSecret(null);
            oidcClientRegistrationDomain.setResponseClientIdIssuedAt(null);
            oidcClientRegistrationDomain.setResponseClientSecretExpiresAt(null);
            oidcClientRegistrationDomain.setResponseRegistrationAccessToken(null);
            oidcClientRegistrationDomain.setResponseRegistrationClientUri(null);
            oidcClientRegistrationDomain.setResponseRegistrationAccessToken(null);
            oidcClientRegistrationDomain.setResponseRegistrationClientUri(null);
            oidcClientRegistrationDomain.setResponseGrantTypeList(null);
            oidcClientRegistrationDomain.setResponseTokenEndpointAuthMethod(null);
            oidcClientRegistrationDomain.setResponseScopeList(null);
            oidcClientRegistrationDomain.setResponseRedirectUriList(null);
            oidcClientRegistrationDomain.setResponseIssuerUri(null);
            oidcClientRegistrationDomain.setResponseJwkSetUri(null);
            oidcClientRegistrationDomain.setResponseTokenUri(null);
            oidcClientRegistrationDomain.setResponseUserInfoUri(null);
            oidcClientRegistrationDomain.setResponseFailureMessage(oidcClientRegistrationException.getMessage());
        }

        oidcClientRegistrationDomain.saveAndFlushHelper();

        return oidcClientRegistrationResponse(oidcClientRegistrationDomain, oidcClientRegistrationDomain.getResponseClientId() == null || oidcClientRegistrationDomain.getResponseClientId().isEmpty());
    }

    public Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, Unit> delete(
            final OAuth2AuthenticationToken oAuth2AuthenticationToken,
            final OidcClientRegistrationRequestDeleteAll clientRegistrationRequestDeleteAll) {

        return validationIdList(iterableList(clientRegistrationRequestDeleteAll.getIdList()))
                .map(list -> list.map(oidcClientRegistration -> {

                    oidcClientRegistration.deleteHelper();

                    return oidcClientRegistration.idHelper();
                }))
                .map(ignore -> unit());
    }
}
