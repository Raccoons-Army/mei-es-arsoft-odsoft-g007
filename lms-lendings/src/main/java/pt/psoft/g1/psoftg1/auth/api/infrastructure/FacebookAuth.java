package pt.psoft.g1.psoftg1.auth.api.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@RequiredArgsConstructor
public class FacebookAuth implements IamAuthentication {

    private final OAuthHelper oAuthHelper;

    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.facebook.scope[0]}")
    private String emailScope;

    @Value("${spring.security.oauth2.client.registration.facebook.scope[1]}")
    private String profileScope;

    @Value("${auth.authorization-url}")
    private String authorizationUrl;

    @Value("${spring.security.oauth2.client.provider.facebook.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.facebook.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.facebook.user-info-uri}")
    private String userInfoUri;

    @Override
    public String getAuthorizationUrl() {
        return oAuthHelper.getAuthorizationUrl(
                authorizationUrl,
                authorizationUri,
                clientId,
                redirectUri,
                emailScope + " " + profileScope
        );
    }

    @Override
    public String handleCallback(String authorizationCode) {
        Map<String, String> tokenResponse = oAuthHelper.getTokenFromAuthorizationCode(
                clientId, clientSecret, authorizationCode, redirectUri, tokenUri
        );
        return tokenResponse.get("access_token");
    }

    @Override
    public Map<String, Object> validateToken(String oauthToken) {
        return oAuthHelper.getUserInfo(oauthToken, userInfoUri);
    }
}
