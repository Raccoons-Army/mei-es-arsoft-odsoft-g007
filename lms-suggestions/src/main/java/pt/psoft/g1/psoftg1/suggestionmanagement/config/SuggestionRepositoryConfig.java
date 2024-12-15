package pt.psoft.g1.psoftg1.suggestionmanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.suggestionmanagement.infraestructure.repositories.impl.SuggestionJpaRepoImpl;
import pt.psoft.g1.psoftg1.suggestionmanagement.infraestructure.repositories.impl.SuggestionMongoRepoImpl;
import pt.psoft.g1.psoftg1.suggestionmanagement.mapper.SuggestionMapper;
import pt.psoft.g1.psoftg1.suggestionmanagement.repositories.SuggestionRepository;

@Configuration
public class SuggestionRepositoryConfig {

    @Bean
    @Profile("jpa")
    public SuggestionRepository jpaSuggestionRepository(EntityManager em, SuggestionMapper suggestionMapper) {
        return new SuggestionJpaRepoImpl(em, suggestionMapper);
    }

    @Bean
    @Profile("mongo")
    public SuggestionRepository mongoSuggestionRepository(MongoTemplate mt, SuggestionMapper suggestionMapper) {
        return new SuggestionMongoRepoImpl(mt, suggestionMapper);
    }
}
