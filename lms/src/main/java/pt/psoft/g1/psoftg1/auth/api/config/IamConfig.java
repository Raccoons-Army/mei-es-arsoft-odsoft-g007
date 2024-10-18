package pt.psoft.g1.psoftg1.auth.api.config;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.auth.api.FacebookAuth;
import pt.psoft.g1.psoftg1.auth.api.GoogleAuth;
import pt.psoft.g1.psoftg1.auth.api.IamAuthentication;

@Configuration
public class IamConfig {

    @Bean
    @Profile("google")
    public IamAuthentication googleAuth() {return new GoogleAuth();}

    @Bean
    @Profile("facebook")
    public IamAuthentication facebookAuth() {
        return new FacebookAuth();
    }
}
