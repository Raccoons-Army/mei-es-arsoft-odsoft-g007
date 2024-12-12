package pt.psoft.g1.psoftg1.suggestedbookmanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.infrastructure.repositories.impl.SuggestedBookJpaRepoImpl;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.infrastructure.repositories.impl.SuggestedBookMongoRepoImpl;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.mapper.SuggestedBookMapper;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.repositories.SuggestedBookRepository;

@Configuration
public class SuggestedBookRepositoryConfig {

    @Bean
    @Profile("jpa")
    public SuggestedBookRepository jpaSuggestedBookRepository(EntityManager em, SuggestedBookMapper suggestedBookMapper) {
        return new SuggestedBookJpaRepoImpl(em, suggestedBookMapper);
    }

    @Bean
    @Profile("mongo")
    public SuggestedBookRepository mongoSuggestedBookRepository(MongoTemplate mt, SuggestedBookMapper suggestedBookMapper) {
        return new SuggestedBookMongoRepoImpl(mt, suggestedBookMapper);
    }
}
