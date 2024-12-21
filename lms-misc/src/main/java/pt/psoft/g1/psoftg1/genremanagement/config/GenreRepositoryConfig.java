package pt.psoft.g1.psoftg1.genremanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.GenreJpaRepoImpl;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.GenreMongoRepoImpl;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

@Configuration
public class GenreRepositoryConfig {

    @Bean
    @Profile("jpa")
    public GenreRepository jpaGenreRepository(EntityManager em, GenreMapper genreMapper) {
        return new GenreJpaRepoImpl(em, genreMapper);
    }

    @Bean
    @Profile("mongo")
    public GenreRepository mongoGenreRepository(MongoTemplate mt, GenreMapper genreMapper) {
        return new GenreMongoRepoImpl(mt, genreMapper);
    }
}
