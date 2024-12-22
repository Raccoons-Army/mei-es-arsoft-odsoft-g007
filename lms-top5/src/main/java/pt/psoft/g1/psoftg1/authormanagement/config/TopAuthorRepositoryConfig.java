package pt.psoft.g1.psoftg1.authormanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.TopAuthorJpaRepoImpl;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.TopAuthorMongoRepoImpl;
import pt.psoft.g1.psoftg1.authormanagement.mapper.TopAuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.repositories.TopAuthorRepository;


@Configuration
public class TopAuthorRepositoryConfig {

    @Bean
    @Profile("jpa")
    public TopAuthorRepository jpaTopAuthorRepository(EntityManager em, TopAuthorMapper topAuthorMapper) {
        return new TopAuthorJpaRepoImpl(em, topAuthorMapper);
    }

    @Bean
    @Profile("mongo")
    public TopAuthorRepository mongoTopAuthorRepository(MongoTemplate mt, TopAuthorMapper topAuthorMapper) {
        return new TopAuthorMongoRepoImpl(mt, topAuthorMapper);
    }

}
