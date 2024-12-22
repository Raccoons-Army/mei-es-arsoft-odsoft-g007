package pt.psoft.g1.psoftg1.bookmanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.Top5BookJpaRepoImpl;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.Top5BookMongoRepoImpl;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.TopBookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.Top5BookRepository;

@Configuration
public class Top5BookRepositoryConfig {

    @Bean
    @Profile("jpa")
    public Top5BookRepository jpaBookRepository(EntityManager em, TopBookMapper topBookMapper) {
        return new Top5BookJpaRepoImpl(em, topBookMapper);
    }

    @Bean
    @Profile("mongo")
    public Top5BookRepository mongoBookRepository(MongoTemplate mt, TopBookMapper topBookMapper) {
        return new Top5BookMongoRepoImpl(mt, topBookMapper);
    }
}
