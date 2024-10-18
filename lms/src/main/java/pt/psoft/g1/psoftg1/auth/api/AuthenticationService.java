package pt.psoft.g1.psoftg1.auth.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final JwtEncoder jwtEncoder;
    private final IamAuthentication tokenValidator;

    public AuthenticationService(JwtEncoder jwtEncoder, IamAuthentication tokenValidator) {
        this.jwtEncoder = jwtEncoder;
        this.tokenValidator = tokenValidator;
    }

    public String getAuthorizationUrl() {
        return tokenValidator.getAuthorizationUrl();
    }

    public String handleLoginCallback(String authorizationCode) {
        String oauthToken = tokenValidator.handleCallback(authorizationCode);
        Map<String, Object> userInfo = tokenValidator.validateToken(oauthToken);

        String userId = userInfo.get("id").toString();
        String username = userInfo.get("name").toString();

        User user = new User(userId, username);
        return generateJwt(user);
    }

    private String generateJwt(User user) {
        Instant now = Instant.now();
        long expiry = 3600L; // Token validity period (e.g., 1 hour)

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("example.io")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(String.format("%s,%s", user.getId(), user.getUsername()))
                .claim("roles", "USER")
                .build();

        log.error("shit is fucked");
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

