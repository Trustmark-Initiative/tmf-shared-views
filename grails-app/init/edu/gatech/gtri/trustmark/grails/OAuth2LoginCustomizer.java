package edu.gatech.gtri.trustmark.grails;

import org.gtri.fj.function.Effect1;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class OAuth2LoginCustomizer implements Customizer<OAuth2LoginConfigurer<HttpSecurity>> {

    private static final OAuth2UserService oauth2UserService = new DefaultOAuth2UserService();

    private final Effect1<OAuth2User> oAuth2UserEffect;

    public OAuth2LoginCustomizer() {
        this.oAuth2UserEffect = oAuth2User -> {
        };
    }

    public OAuth2LoginCustomizer(final Effect1<OAuth2User> oAuth2UserEffect) {
        requireNonNull(oAuth2UserEffect);

        this.oAuth2UserEffect = oAuth2UserEffect;
    }

    @Override
    public void customize(final OAuth2LoginConfigurer<HttpSecurity> httpSecurityOAuth2LoginConfigurer) {

        httpSecurityOAuth2LoginConfigurer.userInfoEndpoint((userInfo -> userInfo
                .userService(oauth2UserRequest -> {
                    final OAuth2User oAuth2User = oauth2UserService.loadUser(oauth2UserRequest);
                    final List<String> roleList = oAuth2User.getAttribute("roles");
                    final List<GrantedAuthority> grantedAuthorityList = AuthorityUtils.createAuthorityList((roleList == null ? Collections.<String>emptyList() : roleList).toArray(new String[]
                            {}));

                    final DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(
                            grantedAuthorityList,
                            oAuth2User.getAttributes(),
                            "preferred_username");

                    oAuth2UserEffect.f(defaultOAuth2User);

                    return defaultOAuth2User;
                })));
    }
}
