package pt.psoft.g1.psoftg1.genremanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.TopGenreJpaRepoImpl;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.TopGenreMongoRepoImpl;
import pt.psoft.g1.psoftg1.genremanagement.mapper.TopGenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.repositories.TopGenreRepository;

@Configuration
public class TopGenreRepositoryConfig {

    @Bean
    @Profile("jpa")
    public TopGenreRepository jpaTopGenreRepository(EntityManager em, TopGenreMapper genreMapper) {
        return new TopGenreJpaRepoImpl(em, genreMapper);
    }

    @Bean
    @Profile("mongo")
    public TopGenreRepository mongoTopGenreRepository(MongoTemplate mt, TopGenreMapper genreMapper) {
        return new TopGenreMongoRepoImpl(mt, genreMapper);
    }
}
