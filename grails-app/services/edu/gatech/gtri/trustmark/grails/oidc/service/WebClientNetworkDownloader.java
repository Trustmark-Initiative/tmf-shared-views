package edu.gatech.gtri.trustmark.grails.oidc.service;

import edu.gatech.gtri.trustmark.v1_0.FactoryLoader;
import edu.gatech.gtri.trustmark.v1_0.io.HttpResponse;
import edu.gatech.gtri.trustmark.v1_0.io.NetworkDownloader;
import edu.gatech.gtri.trustmark.v1_0.io.SessionResolver;
import org.gtri.fj.data.Option;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;
import static org.gtri.fj.data.Option.none;
import static org.gtri.fj.data.Option.some;

public class WebClientNetworkDownloader implements NetworkDownloader {

    private final WebClient webClient;

    public WebClientNetworkDownloader(
            final WebClient webClient) {

        requireNonNull(webClient, "webClient");

        this.webClient = webClient;
    }

    @Override
    public HttpResponse download(
            final URL url) throws IOException {

        requireNonNull(url);

        return download(url, none());
    }

    @Override
    public HttpResponse download(
            final URL url,
            final String accept) throws IOException {

        requireNonNull(url);
        requireNonNull(accept);

        return download(url, some(accept));
    }

    private HttpResponse download(
            final URL url,
            final Option<String> acceptOption) throws IOException {
        try {
            requireNonNull(url);
            requireNonNull(acceptOption);

            final SessionResolver sessionResolver = SessionResolver.getSessionResolver();

            final ResponseEntity<String> responseEntity = webClient.get()
                    .uri(url.toString())
                    .headers(headers -> acceptOption.forEach(accept -> headers.set(HttpHeaders.ACCEPT, accept)))
                    .acceptCharset(StandardCharsets.UTF_8)
                    .cookies(cookies -> {
                        if (sessionResolver != null) {
                            cookies.set("Cookie", "JSESSIONID=" + sessionResolver.getSessionId());
                        }
                    })
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            final HttpResponse httpResponse = FactoryLoader.getInstance(HttpResponse.class);

            httpResponse.setHeaders(responseEntity.getHeaders());
            httpResponse.setContentType(responseEntity.getHeaders().getContentType() == null ? null : responseEntity.getHeaders().getContentType().toString());
            httpResponse.setResponseCode(responseEntity.getStatusCode().value());
            httpResponse.setResponseMessage(responseEntity.getStatusCode().getReasonPhrase());
            httpResponse.setData(responseEntity.getBody() == null ? new byte[]{} : responseEntity.getBody().getBytes(StandardCharsets.UTF_8));

            return httpResponse;

        } catch (final Exception exception) {
            exception.printStackTrace();
            throw new IOException(exception);
        }
    }
}
