package pt.psoft.g1.psoftg1.auth.api;

import java.util.Map;

public interface IamAuthentication {

    /**
     * Validates an OAuth token and retrieves user information from the provider.
     *
     * @param oauthToken The OAuth token to be validated.
     * @return A Map containing user information.
     */
    Map<String, Object> validateToken(String oauthToken);

    /**
     * Generates the authorization URL to redirect the user for authentication.
     *
     * @return The authorization URL as a String.
     */
    String getAuthorizationUrl();

    String handleCallback(String authorizationCode);
}
