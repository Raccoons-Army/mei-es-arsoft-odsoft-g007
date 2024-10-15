package pt.psoft.g1.psoftg1.genremanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.GenreJpaRepoImpl;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.GenreMongoRepoImpl;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

@Configuration
public class GenreRepositoryConfig {

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "jpa")
    public GenreRepository jpaGenreRepository(EntityManager em) {
        return new GenreJpaRepoImpl(em);
    }

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "mongodb")
    public GenreRepository mongoGenreRepository(MongoTemplate mongoTemplate) {
        return new GenreMongoRepoImpl(mongoTemplate);
    }
}
