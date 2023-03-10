package edu.gatech.gtri.trustmark.grails.oidc.service;

import edu.gatech.gtri.trustmark.grails.oidc.domain.OidcClientRegistration;
import edu.gatech.gtri.trustmark.v1_0.web.validation.ValidationMessage;
import org.grails.web.json.JSONArray;
import org.gtri.fj.data.List;
import org.gtri.fj.data.NonEmptyList;
import org.gtri.fj.data.Validation;

import static edu.gatech.gtri.trustmark.v1_0.web.validation.ValidationUtility.mustBeNonNullAndDistinctAndValid;
import static edu.gatech.gtri.trustmark.v1_0.web.validation.ValidationUtility.mustBeNonNullAndLength;
import static edu.gatech.gtri.trustmark.v1_0.web.validation.ValidationUtility.mustBeNonNullAndUniqueAndLength;
import static edu.gatech.gtri.trustmark.v1_0.web.validation.ValidationUtility.mustBeReference;
import static org.gtri.fj.lang.LongUtility.longOrd;

public class OidcClientRegistrationUtility {

    public static OidcClientRegistrationResponse oidcClientRegistrationResponse(
            final OidcClientRegistration oidcClientRegistration,
            final boolean editable) {

        return new OidcClientRegistrationResponse(
                oidcClientRegistration.idHelper(),
                oidcClientRegistration.getRequestInitialAccessToken(),
                oidcClientRegistration.getRequestClientName(),
                oidcClientRegistration.getRequestClientUri(),
                oidcClientRegistration.getResponseClientId(),
                oidcClientRegistration.getResponseClientIdIssuedAt(),
                oidcClientRegistration.getResponseClientSecretExpiresAt(),
                oidcClientRegistration.getResponseRegistrationAccessToken(),
                oidcClientRegistration.getResponseRegistrationClientUri(),
                oidcClientRegistration.getResponseGrantTypeList() == null ? null : new JSONArray(oidcClientRegistration.getResponseGrantTypeList()),
                oidcClientRegistration.getResponseTokenEndpointAuthMethod(),
                oidcClientRegistration.getResponseScopeList() == null ? null : new JSONArray(oidcClientRegistration.getResponseScopeList()),
                oidcClientRegistration.getResponseRedirectUriList() == null ? null : new JSONArray(oidcClientRegistration.getResponseRedirectUriList()),
                oidcClientRegistration.getResponseIssuerUri(),
                oidcClientRegistration.getResponseJwkSetUri(),
                oidcClientRegistration.getResponseTokenUri(),
                oidcClientRegistration.getResponseUserInfoUri(),
                oidcClientRegistration.getResponseFailureMessage(),
                editable);
    }

    public static Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, OidcClientRegistration> validationId(final long id) {

        return mustBeReference(
                OidcClientRegistrationField.id,
                OidcClientRegistration::findByIdHelper,
                id);
    }

    public static Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, OidcClientRegistration> validationIdEditable(final long id) {

        return mustBeReference(
                OidcClientRegistrationField.id,
                OidcClientRegistration::findByIdHelper,
                id,
                oidcClientRegistration -> oidcClientRegistration.getResponseClientId() == null || oidcClientRegistration.getResponseClientId().isEmpty());
    }

    public static Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, String> validationRequestInitialAccessToken(final String value) {

        return mustBeNonNullAndLength(
                OidcClientRegistrationField.requestInitialAccessToken,
                1,
                1000,
                value);
    }

    public static Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, String> validationRequestClientName(final String value) {

        return mustBeNonNullAndUniqueAndLength(
                OidcClientRegistrationField.requestClientName,
                OidcClientRegistration::findByRequestClientNameHelper,
                1,
                1000,
                value);
    }

    public static Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, String> validationRequestClientName(final long id, final String value) {

        return mustBeNonNullAndUniqueAndLength(
                OidcClientRegistrationField.requestClientName,
                valueInner -> OidcClientRegistration.findByRequestClientNameHelper(valueInner).filter(oidcClientRegistration -> oidcClientRegistration.idHelper() != id),
                1,
                1000,
                value);
    }

    public static Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, String> validationRequestClientUri(final String value) {

        return mustBeNonNullAndUniqueAndLength(
                OidcClientRegistrationField.requestClientUri,
                OidcClientRegistration::findByRequestClientUriHelper,
                1,
                1000,
                value);
    }

    public static Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, String> validationRequestClientUri(final long id, final String value) {

        return mustBeNonNullAndUniqueAndLength(
                OidcClientRegistrationField.requestClientUri,
                valueInner -> OidcClientRegistration.findByRequestClientUriHelper(valueInner).filter(oidcClientRegistration -> oidcClientRegistration.idHelper() != id),
                1,
                1000,
                value);
    }

    public static Validation<NonEmptyList<ValidationMessage<OidcClientRegistrationField>>, List<OidcClientRegistration>> validationIdList(final List<Long> idList) {

        return mustBeNonNullAndDistinctAndValid(
                OidcClientRegistrationField.idList,
                idList,
                longOrd,
                id -> validationId(id));
    }
}
