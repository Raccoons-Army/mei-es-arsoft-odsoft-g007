package pt.psoft.g1.psoftg1.auth.api;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.psoft.g1.psoftg1.auth.api.service.AuthenticationService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public ResponseEntity<Void> redirectToProvider() {
        String authorizationUrl = authenticationService.getAuthorizationUrl();
        if (authorizationUrl == null) {
            log.error("Authorization URL could not be retrieved.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authorizationUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleProviderCallback(
            @RequestParam("code") String authorizationCode) {
        try {
            String jwtToken = authenticationService.handleLoginCallback(authorizationCode);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).body(jwtToken);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
}
