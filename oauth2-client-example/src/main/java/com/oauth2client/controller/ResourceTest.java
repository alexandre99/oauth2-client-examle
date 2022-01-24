package com.oauth2client.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.oauth2client.component.TokenManager;

@RequestMapping("/oauth2/client")
@RestController
public class ResourceTest {

    private final TokenManager tokenManager;

    private final RestTemplate restTemplate;

    public ResourceTest(TokenManager tokenManager, RestTemplate restTemplate) {
        this.tokenManager = tokenManager;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<String> oauth2Client() {
        String token = tokenManager.getAccessToken();

        String url = "http://localhost:5000/user?page=0&size=10";

        final var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        var response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return ResponseEntity.ok(response.getBody());
    }

}
