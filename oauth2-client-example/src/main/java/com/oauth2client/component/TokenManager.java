package com.oauth2client.component;

import java.util.Optional;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Component
public class TokenManager {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    private final ClientRegistrationRepository registrationRepository;

    public TokenManager(final OAuth2AuthorizedClientManager authorizedClientManager,
                        final ClientRegistrationRepository registrationRepository) {
        this.authorizedClientManager = authorizedClientManager;
        this.registrationRepository = registrationRepository;
    }

    public String getAccessToken() {
        final var resourceIdTest = registrationRepository.findByRegistrationId("RESOURCE_ID_TEST");
        final var authorizeRequest =
                OAuth2AuthorizeRequest.withClientRegistrationId(resourceIdTest.getRegistrationId())
                        .principal(resourceIdTest.getClientId())
                        .build();

        final var optionalAuthorizedClient =
                Optional.ofNullable(this.authorizedClientManager.authorize(authorizeRequest));

        final var authorizedClient = optionalAuthorizedClient.orElseThrow();

        return authorizedClient.getAccessToken().getTokenValue();
    }

}
