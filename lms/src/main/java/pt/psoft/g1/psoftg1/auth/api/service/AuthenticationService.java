package pt.psoft.g1.psoftg1.auth.api.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.auth.api.infrastructure.IamAuthentication;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.CreateIamUserRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final JwtEncoder jwtEncoder;
    private final IamAuthentication tokenValidator;
    private final UserService userService;

    public String getAuthorizationUrl() {
        return tokenValidator.getAuthorizationUrl();
    }

    public String handleLoginCallback(String authorizationCode) {
        try {
            // Exchange authorization code for an OAuth token
            String oauthToken = tokenValidator.handleCallback(authorizationCode);
            // Validate the token and retrieve user information
            Map<String, Object> userInfo = tokenValidator.validateToken(oauthToken);

            String username = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");

            if (username == null || name == null) {
                throw new IllegalStateException("User info is missing required fields (email or name).");
            }

            Optional<User> user = userService.findByUsername(username);

            User iamUser;
            if (user.isEmpty()) {
                CreateIamUserRequest request = new CreateIamUserRequest(username, name);
                iamUser = userService.createIam(request);
            } else {
                iamUser = user.get();
            }

            return generateJwt(iamUser);

        } catch (Exception e) {
            log.error("Error during login callback: " + e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }
    }

    private String generateJwt(User user) {
        Instant now = Instant.now();
        long expiry = 3600L; // Token validity period (e.g., 1 hour)

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("example.io")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(String.format("%s,%s", user.getPk(), user.getUsername()))
                .claim("roles", "USER")
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

