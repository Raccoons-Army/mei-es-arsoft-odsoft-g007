package pt.psoft.g1.psoftg1.usermanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.UserJpaRepoImpl;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.UserMongoRepoImpl;
import pt.psoft.g1.psoftg1.usermanagement.mapper.UserMapper;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

@Configuration
public class UserRepositoryConfig {

    @Bean
    @Profile("jpa")
    public UserRepository jpaUserRepository(EntityManager em, UserMapper userMapper) {
        return new UserJpaRepoImpl(em, userMapper);
    }

    @Bean
    @Profile("mongo")
    public UserRepository mongoUserRepository(MongoTemplate mt) {
        return new UserMongoRepoImpl(mt);
    }
}
