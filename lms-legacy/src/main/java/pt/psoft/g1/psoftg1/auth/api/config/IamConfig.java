package pt.psoft.g1.psoftg1.auth.api.config;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.psoft.g1.psoftg1.auth.api.infrastructure.FacebookAuth;
import pt.psoft.g1.psoftg1.auth.api.infrastructure.GoogleAuth;
import pt.psoft.g1.psoftg1.auth.api.infrastructure.IamAuthentication;
import pt.psoft.g1.psoftg1.auth.api.infrastructure.OAuthHelper;

@Configuration
public class IamConfig {

    @Bean
    public OAuthHelper oAuthHelper() {
        return new OAuthHelper();
    }

    @Bean
    @Profile("google")
    public IamAuthentication googleAuth(OAuthHelper oAuthHelper) {return new GoogleAuth(oAuthHelper);}

    @Bean
    @Profile("facebook")
    public IamAuthentication facebookAuth(OAuthHelper oAuthHelper) {
        return new FacebookAuth(oAuthHelper);
    }
}
