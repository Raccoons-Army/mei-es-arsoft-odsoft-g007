package pt.psoft.g1.psoftg1.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class FacebookAuth implements IamAuthentication {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.authorization-url}")
    private String authorizationUrl;

    @Value("${spring.security.oauth2.client.provider.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.user-info-uri}")
    private String userInfoUri;

    @Override
    public String getAuthorizationUrl() {
        // Build the Facebook OAuth2 authorization URL
        return String.format(authorizationUrl, authorizationUri, clientId, redirectUri);
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
        // Retrieve user info using the access token
        return getUserInfo(oauthToken);
    }

    private Map<String, String> getTokenFromAuthorizationCode(String authorizationCode) {
        // Prepare the request body for exchanging the authorization code
        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("code", authorizationCode);
        body.put("redirect_uri", redirectUri);

        // Prepare headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        // Send POST request to Facebook's token endpoint
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);

        // Return the token response
        return response.getBody();
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        // Prepare the headers with the access token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Make a GET request to Facebook's user info endpoint
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

        // Return the user info as a map
        return response.getBody();
    }
}
