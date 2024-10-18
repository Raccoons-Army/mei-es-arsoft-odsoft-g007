package pt.psoft.g1.psoftg1.auth.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.time.Instant;
import java.util.Map;

@Service
public class AuthenticationService {

    private final JwtEncoder jwtEncoder;
    private final IamAuthentication tokenValidator;

    @Autowired
    public AuthenticationService(JwtEncoder jwtEncoder, IamAuthentication tokenValidator) {
        this.jwtEncoder = jwtEncoder;
        this.tokenValidator = tokenValidator;
    }

    /**
     * Authenticates a user based on the OAuth token provided and returns a JWT token.
     *
     * @param oauthToken The OAuth token provided by the client.
     * @return The generated JWT token for the authenticated user.
     */
    public String authenticateAndGenerateJwt(String oauthToken) {
        // Use the injected token validator to validate the token and retrieve user info
        Map<String, Object> userInfo = tokenValidator.validateToken(oauthToken);

        // Extract necessary user information (assuming 'id' and 'name' keys exist in the response)
        String userId = userInfo.get("id").toString();
        String username = userInfo.get("name").toString();

        // Here you could use a User object (domain-specific) or work directly with userInfo if needed
        User user = new User(userId, username);

        // Generate the JWT token for the authenticated user
        return generateJwt(user);
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user for whom the JWT is being generated.
     * @return The JWT token as a String.
     */
    private String generateJwt(User user) {
        Instant now = Instant.now();
        long expiry = 3600L; // Token validity period (e.g., 1 hour)

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("example.io")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(String.format("%s,%s", user.getId(), user.getUsername()))
                .claim("roles", "USER") // Assign roles or scopes if needed
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

