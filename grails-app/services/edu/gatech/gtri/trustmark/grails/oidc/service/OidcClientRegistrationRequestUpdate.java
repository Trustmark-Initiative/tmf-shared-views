package edu.gatech.gtri.trustmark.grails.oidc.service;

public class OidcClientRegistrationRequestUpdate {

    private long id;
    private String requestInitialAccessToken;
    private String requestClientName;
    private String requestClientUri;

    public OidcClientRegistrationRequestUpdate() {
    }

    public OidcClientRegistrationRequestUpdate(
            final long id,
            final String requestInitialAccessToken,
            final String requestClientName,
            final String requestClientUri) {
        this.id = id;
        this.requestInitialAccessToken = requestInitialAccessToken;
        this.requestClientName = requestClientName;
        this.requestClientUri = requestClientUri;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
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
