package pt.psoft.g1.psoftg1.authormanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.AuthorJpaRepoImpl;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.AuthorMongoRepoImpl;
import pt.psoft.g1.psoftg1.authormanagement.mapper.AuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;


@Configuration
public class AuthorRepositoryConfig {

    @Bean
    @Profile("jpa")
    public AuthorRepository jpaAuthorRepository(EntityManager em, AuthorMapper authorMapper) {
        return new AuthorJpaRepoImpl(em, authorMapper);
    }

    @Bean
    @Profile("mongo")
    public AuthorRepository mongoAuthorRepository(MongoTemplate mt, AuthorMapper authorMapper) {
        return new AuthorMongoRepoImpl(mt, authorMapper);
    }

}
