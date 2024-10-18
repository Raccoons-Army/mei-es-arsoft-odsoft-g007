package pt.psoft.g1.psoftg1.auth.api;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@RequiredArgsConstructor
public class GoogleAuth implements IamAuthentication {

    private final Logger log = LoggerFactory.getLogger(GoogleAuth.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.scope[0]}")
    private String emailScope;

    @Value("${spring.security.oauth2.client.registration.google.scope[1]}")
    private String profileScope;

    @Value("${auth.authorization-url}")
    private String authorizationUrl;

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUri;

    @Override
    public String getAuthorizationUrl() {
        // Construct the authorization URL
        return String.format(authorizationUrl,
                authorizationUri,
                clientId,
                URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
                URLEncoder.encode(emailScope + " " + profileScope, StandardCharsets.UTF_8));
    }

    @Override
    public String handleCallback(String authorizationCode) {
        // Exchange the authorization code for an access token
        Map<String, String> tokenResponse = getTokenFromAuthorizationCode(authorizationCode);

        // Return the access token if successful
        return tokenResponse.get("access_token");
    }

    @Override
    public Map<String, Object> validateToken(String oauthToken) {
        return getUserInfo(oauthToken);
    }

    private Map<String, String> getTokenFromAuthorizationCode(String authorizationCode) {
        // Prepare the request body for exchanging the authorization code
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", authorizationCode);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        // Prepare headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Send POST request to Google's token endpoint
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response;

        try {
            response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);

            // Check if the response status is OK (200)
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to exchange authorization code: " + response.getStatusCode());
            }

            // Return the token response
            return response.getBody();

        } catch (Exception e) {
            log.error("Unexpected error while exchanging authorization code", e);
            // Handle other exceptions
            throw new RuntimeException("Unexpected error while exchanging authorization code", e);
        }
    }


    private Map<String, Object> getUserInfo(String accessToken) {
        // Prepare the headers with the access token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", "application/json");  // Set the Accept header to expect JSON

        // Make a GET request to Google's user info endpoint
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response;

        try {
            response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

            // Check if the response is OK (200)
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();  // Return the user info as a map
            } else {
                log.error("Failed to retrieve user info");
                throw new RuntimeException("Failed to retrieve user info: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Unexpected error while exchanging authorization code", e);
            // Handle other exceptions
            throw new RuntimeException("Unexpected error while retrieving user info", e);
        }
    }
}
