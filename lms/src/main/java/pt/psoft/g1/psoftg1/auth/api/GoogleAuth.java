package pt.psoft.g1.psoftg1.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleAuth implements IamAuthentication {


    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> validateToken(String oauthToken) {
        String url = "https://oauth2.googleapis.com/tokeninfo?access_token=" + oauthToken;
        return restTemplate.getForObject(url, Map.class);
    }

    @Override
    public String getAuthorizationUrl() {
        return String.format(
                "https://accounts.google.com/o/oauth2/v2/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=email profile",
                clientId, redirectUri
        );
    }
}
