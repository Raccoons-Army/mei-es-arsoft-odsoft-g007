package pt.psoft.g1.psoftg1.auth.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.psoft.g1.psoftg1.auth.api.FacebookAuth;
import pt.psoft.g1.psoftg1.auth.api.GoogleAuth;
import pt.psoft.g1.psoftg1.auth.api.IamAuthentication;

@Configuration
public class IamConfig {

    @Bean
    @ConditionalOnProperty(name = "app.iam.provider", havingValue = "google")
    public IamAuthentication googleAuth() {return new GoogleAuth();}

    @Bean
    @ConditionalOnProperty(name = "app.iam.provider", havingValue = "facebook")
    public IamAuthentication facebookAuth() {
        return new FacebookAuth();
    }
}
