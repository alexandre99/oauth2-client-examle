package com.oauth2client.config;

import java.util.Map;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public ClientRegistrationRepository getRegistration() {
        final var registration = ClientRegistration
                .withRegistrationId("RESOURCE_ID_TEST")
                .tokenUri("http://localhost:5000/oauth/token")
                .clientName("local_test")
                .clientId("client")
                .clientSecret("123")
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .scope("all")
                .build();
        return new InMemoryClientRegistrationRepository(registration);
    }

    @Bean
    public OAuth2AuthorizedClientManager auth2AuthorizedClientManager(
            final ClientRegistrationRepository clientRegistrationRepository,
            final OAuth2AuthorizedClientService authorizedClientService
    ) {
        final var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
                .builder()
                .password()
                .refreshToken() // Only if your API has refresh token
                .build();

        final AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());

        return authorizedClientManager;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper() {
        return authorizeRequest ->
                Map.of(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, "admin", OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, "123456");
    }
}
