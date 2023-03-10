package edu.gatech.gtri.trustmark.grails.oidc.service;

import java.time.LocalDateTime;
import java.util.List;

public class OidcClientRegistrationResponse {

    private final long id;
    private final String requestInitialAccessToken;
    private final String requestClientName;
    private final String requestClientUri;
    private final String responseClientId;
    private final LocalDateTime responseClientIdIssuedAt;
    private final LocalDateTime responseClientSecretExpiresAt;
    private final String responseRegistrationAccessToken;
    private final String responseRegistrationClientUri;
    private final List<String> responseGrantTypeList;
    private final String responseTokenEndpointAuthMethod;
    private final List<String> responseScopeList;
    private final List<String> responseRedirectUriList;
    private final String responseIssuerUri;
    private final String responseJwkSetUri;
    private final String responseTokenUri;
    private final String responseUserInfoUri;
    private final String responseFailureMessage;
    private final boolean editable;

    public OidcClientRegistrationResponse(
            final long id,
            final String requestInitialAccessToken,
            final String requestClientName,
            final String requestClientUri,
            final String responseClientId,
            final LocalDateTime responseClientIdIssuedAt,
            final LocalDateTime responseClientSecretExpiresAt,
            final String responseRegistrationAccessToken,
            final String responseRegistrationClientUri,
            final List<String> responseGrantTypeList,
            final String responseTokenEndpointAuthMethod,
            final List<String> responseScopeList,
            final List<String> responseRedirectUriList,
            final String responseIssuerUri,
            final String responseJwkSetUri,
            final String responseTokenUri,
            final String responseUserInfoUri,
            final String responseFailureMessage,
            final boolean editable) {
        this.id = id;
        this.requestInitialAccessToken = requestInitialAccessToken;
        this.requestClientName = requestClientName;
        this.requestClientUri = requestClientUri;
        this.responseClientId = responseClientId;
        this.responseClientIdIssuedAt = responseClientIdIssuedAt;
        this.responseClientSecretExpiresAt = responseClientSecretExpiresAt;
        this.responseRegistrationAccessToken = responseRegistrationAccessToken;
        this.responseRegistrationClientUri = responseRegistrationClientUri;
        this.responseGrantTypeList = responseGrantTypeList;
        this.responseTokenEndpointAuthMethod = responseTokenEndpointAuthMethod;
        this.responseScopeList = responseScopeList;
        this.responseRedirectUriList = responseRedirectUriList;
        this.responseIssuerUri = responseIssuerUri;
        this.responseJwkSetUri = responseJwkSetUri;
        this.responseTokenUri = responseTokenUri;
        this.responseUserInfoUri = responseUserInfoUri;
        this.responseFailureMessage = responseFailureMessage;
        this.editable = editable;
    }

    public long getId() {
        return id;
    }

    public String getRequestInitialAccessToken() {
        return requestInitialAccessToken;
    }

    public String getRequestClientName() {
        return requestClientName;
    }

    public String getRequestClientUri() {
        return requestClientUri;
    }

    public String getResponseClientId() {
        return responseClientId;
    }

    public LocalDateTime getResponseClientIdIssuedAt() {
        return responseClientIdIssuedAt;
    }

    public LocalDateTime getResponseClientSecretExpiresAt() {
        return responseClientSecretExpiresAt;
    }

    public String getResponseRegistrationAccessToken() {
        return responseRegistrationAccessToken;
    }

    public String getResponseRegistrationClientUri() {
        return responseRegistrationClientUri;
    }

    public List<String> getResponseGrantTypeList() {
        return responseGrantTypeList;
    }

    public String getResponseTokenEndpointAuthMethod() {
        return responseTokenEndpointAuthMethod;
    }

    public List<String> getResponseScopeList() {
        return responseScopeList;
    }

    public List<String> getResponseRedirectUriList() {
        return responseRedirectUriList;
    }

    public String getResponseIssuerUri() {
        return responseIssuerUri;
    }

    public String getResponseJwkSetUri() {
        return responseJwkSetUri;
    }

    public String getResponseTokenUri() {
        return responseTokenUri;
    }

    public String getResponseUserInfoUri() {
        return responseUserInfoUri;
    }

    public String getResponseFailureMessage() {
        return responseFailureMessage;
    }

    public boolean isEditable() {
        return editable;
    }
}
