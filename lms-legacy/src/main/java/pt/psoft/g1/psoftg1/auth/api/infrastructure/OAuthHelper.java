package pt.psoft.g1.psoftg1.auth.api.infrastructure;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
public class OAuthHelper {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger log = LoggerFactory.getLogger(OAuthHelper.class);

    public String getAuthorizationUrl(String authorizationUrlTemplate, String authorizationUri, String clientId, String redirectUri, String scopes) {
        return String.format(
                authorizationUrlTemplate,
                authorizationUri,
                clientId,
                URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
                URLEncoder.encode(scopes, StandardCharsets.UTF_8)
        );
    }

    public Map<String, String> getTokenFromAuthorizationCode(String clientId, String clientSecret, String authorizationCode, String redirectUri, String tokenUri) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", authorizationCode);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response;

        try {
            response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to exchange authorization code: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (Exception e) {
            log.error("Unexpected error while exchanging authorization code", e);
            throw new RuntimeException("Unexpected error while exchanging authorization code", e);
        }
    }

    public Map<String, Object> getUserInfo(String accessToken, String userInfoUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response;

        try {
            response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                log.error("Failed to retrieve user info");
                throw new RuntimeException("Failed to retrieve user info: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Unexpected error while retrieving user info", e);
            throw new RuntimeException("Unexpected error while retrieving user info", e);
        }
    }
}
