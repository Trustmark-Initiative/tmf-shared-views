package edu.gatech.gtri.trustmark.grails.oidc.service;

import edu.gatech.gtri.trustmark.v1_0.FactoryLoader;
import edu.gatech.gtri.trustmark.v1_0.io.NetworkDownloader;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

public final class WebClientConfigurer {

    private static final OidcClientRegistrationRepository oidcClientRegistrationRepository = new OidcClientRegistrationRepository();

    public static WebClient webClient() {
        final WebClient webClient = WebClient
                .builder()
                .defaultRequest(requestHeadersSpec -> requestHeadersSpec.attributes(attributesConsumer -> {
                    oidcClientRegistrationRepository
                            .findByUriTemplate((String) attributesConsumer.get("org.springframework.web.reactive.function.client.WebClient.uriTemplate"))
                            .forEach(clientRegistration -> {
                                ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(clientRegistration.getRegistrationId()).accept(attributesConsumer);
                            });
                }))
                .filter(new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                        new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                                oidcClientRegistrationRepository,
                                new InMemoryReactiveOAuth2AuthorizedClientService(oidcClientRegistrationRepository))))
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024*1024*50))
                        .build())
                .build();

        FactoryLoader.register(NetworkDownloader.class, new WebClientNetworkDownloader(webClient));

        return webClient;
    }
}
