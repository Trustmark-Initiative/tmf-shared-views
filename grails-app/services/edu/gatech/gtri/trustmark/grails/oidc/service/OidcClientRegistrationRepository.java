package edu.gatech.gtri.trustmark.grails.oidc.service;

import edu.gatech.gtri.trustmark.grails.oidc.domain.OidcClientRegistration;
import org.grails.web.json.JSONArray;
import org.gtri.fj.data.Option;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class OidcClientRegistrationRepository implements ReactiveClientRegistrationRepository {

    public Option<ClientRegistration> findByUriTemplate(final String uriTemplate) {
        requireNonNull("uriTemplate", uriTemplate);

        return OidcClientRegistration.withTransactionHelper(() -> OidcClientRegistration
                .findByUriTemplateHelper(uriTemplate)
                .map(this::clientRegistration));
    }

    @Override
    public Mono<ClientRegistration> findByRegistrationId(final String registrationId) {
        requireNonNull("registrationId", registrationId);

        return OidcClientRegistration.withTransactionHelper(() -> OidcClientRegistration
                .findByResponseClientIdHelper(registrationId)
                .filter(oidcClientRegistration -> oidcClientRegistration.getResponseFailureMessage() == null)
                .map(this::clientRegistration)
                .map(Mono::just)
                .orSome(Mono.empty()));
    }

    private ClientRegistration clientRegistration(final OidcClientRegistration oidcClientRegistration) {
        requireNonNull(oidcClientRegistration);

        final List<String> responseRedirectUriList = (List<String>) new JSONArray(oidcClientRegistration.getResponseRedirectUriList());
        final String[] responseScopeList = (String[]) new JSONArray(oidcClientRegistration.getResponseScopeList()).toArray(new String[]{});

        ClientRegistration.Builder clientRegistrationBuilder = ClientRegistration
                .withRegistrationId(oidcClientRegistration.getResponseClientId())
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientId(oidcClientRegistration.getResponseClientId())
                .clientName(oidcClientRegistration.getRequestClientName())
                .clientSecret(oidcClientRegistration.getResponseClientSecret())
                .issuerUri(oidcClientRegistration.getResponseIssuerUri())
                .jwkSetUri(oidcClientRegistration.getResponseJwkSetUri())
                .tokenUri(oidcClientRegistration.getResponseTokenUri())
                .scope(responseScopeList)
                .userInfoAuthenticationMethod(AuthenticationMethod.HEADER)
                .userInfoUri(oidcClientRegistration.getResponseUserInfoUri());

        if (!responseRedirectUriList.isEmpty()) {
            clientRegistrationBuilder = clientRegistrationBuilder.redirectUri(responseRedirectUriList.get(0));
        }

        return clientRegistrationBuilder.build();
    }
}
