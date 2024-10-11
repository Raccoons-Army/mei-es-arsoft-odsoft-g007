package pt.psoft.g1.psoftg1.authormanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.AuthorJpaRepoImpl;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.AuthorMongoRepoImpl;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

@Configuration
public class AuthorRepositoryConfig {

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "jpa")
    public AuthorRepository jpaAuthorRepository(EntityManager em) {
        return new AuthorJpaRepoImpl(em);
    }

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "mongodb")
    public AuthorRepository mongoAuthorRepository(MongoTemplate mongoTemplate) {
        return new AuthorMongoRepoImpl(mongoTemplate);
    }
}
