package pt.psoft.g1.psoftg1.bookmanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.TopBookJpaRepoImpl;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.TopBookMongoRepoImpl;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.TopBookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.TopBookRepository;

@Configuration
public class TopBookRepositoryConfig {

    @Bean
    @Profile("jpa")
    public TopBookRepository jpaTopBookRepository(EntityManager em, TopBookMapper topBookMapper) {
        return new TopBookJpaRepoImpl(em, topBookMapper);
    }

    @Bean
    @Profile("mongo")
    public TopBookRepository mongoTopBookRepository(MongoTemplate mt, TopBookMapper topBookMapper) {
        return new TopBookMongoRepoImpl(mt, topBookMapper);
    }
}
