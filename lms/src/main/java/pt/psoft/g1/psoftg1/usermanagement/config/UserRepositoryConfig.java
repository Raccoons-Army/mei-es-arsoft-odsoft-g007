package pt.psoft.g1.psoftg1.usermanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.UserJpaRepoImpl;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.UserMongoRepoImpl;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

@Configuration
public class UserRepositoryConfig {

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "jpa")
    public UserRepository jpaUserRepository(EntityManager em) {
        return new UserJpaRepoImpl(em);
    }

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "mongodb")
    public UserRepository mongoUserRepository(MongoTemplate mt) {
        return new UserMongoRepoImpl(mt);
    }
}
