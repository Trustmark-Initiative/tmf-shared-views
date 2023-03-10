package edu.gatech.gtri.trustmark.grails.oidc.service;

public class OidcClientRegistrationRequestInsert {

    private String requestInitialAccessToken;
    private String requestClientName;
    private String requestClientUri;

    public OidcClientRegistrationRequestInsert() {
    }

    public OidcClientRegistrationRequestInsert(
            final String requestInitialAccessToken,
            final String requestClientName,
            final String requestClientUri) {

        this.requestInitialAccessToken = requestInitialAccessToken;
        this.requestClientName = requestClientName;
        this.requestClientUri = requestClientUri;
    }

    public String getRequestInitialAccessToken() {
        return requestInitialAccessToken;
    }

    public void setRequestInitialAccessToken(final String requestInitialAccessToken) {
        this.requestInitialAccessToken = requestInitialAccessToken;
    }

    public String getRequestClientName() {
        return requestClientName;
    }

    public void setRequestClientName(final String requestClientName) {
        this.requestClientName = requestClientName;
    }

    public String getRequestClientUri() {
        return requestClientUri;
    }

    public void setRequestClientUri(final String requestClientUri) {
        this.requestClientUri = requestClientUri;
    }
}
