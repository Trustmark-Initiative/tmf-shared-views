package edu.gatech.gtri.trustmark.grails.oidc.service;

import java.util.List;

public class OidcClientRegistrationRequestDeleteAll {

    private List<Long> idList;

    public OidcClientRegistrationRequestDeleteAll() {
    }

    public OidcClientRegistrationRequestDeleteAll(
            final List<Long> idList) {

        this.idList = idList;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(final List<Long> idList) {
        this.idList = idList;
    }
}
