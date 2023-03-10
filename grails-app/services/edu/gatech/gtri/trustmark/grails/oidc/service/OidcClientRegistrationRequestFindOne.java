package edu.gatech.gtri.trustmark.grails.oidc.service;

public class OidcClientRegistrationRequestFindOne {

    private long id;

    public OidcClientRegistrationRequestFindOne() {
    }

    public OidcClientRegistrationRequestFindOne(
            final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
