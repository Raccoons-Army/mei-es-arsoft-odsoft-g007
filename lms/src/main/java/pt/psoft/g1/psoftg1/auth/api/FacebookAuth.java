package pt.psoft.g1.psoftg1.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
public class FacebookAuth implements IamAuthentication {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> validateToken(String oauthToken) {
        String url = "https://graph.facebook.com/me?access_token=" + oauthToken + "&fields=id,name,email";
        return restTemplate.getForObject(url, Map.class);
    }
}
