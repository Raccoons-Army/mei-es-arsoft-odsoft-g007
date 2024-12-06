package pt.psoft.g1.psoftg1.recommendationmanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.recommendationmanagement.infrastructure.repositories.impl.RecommendationJpaRepoImpl;
import pt.psoft.g1.psoftg1.recommendationmanagement.infrastructure.repositories.impl.RecommendationMongoRepoImpl;
import pt.psoft.g1.psoftg1.recommendationmanagement.mapper.RecommendationMapper;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;

@Configuration
public class RecommendationRepositoryConfig {

    @Bean
    @Profile("jpa")
    public RecommendationRepository jpaLendingRepository(EntityManager em, RecommendationMapper recommendationMapper) {
        return new RecommendationJpaRepoImpl(em, recommendationMapper);
    }

    @Bean
    @Profile("mongo")
    public RecommendationRepository mongoLendingRepository(MongoTemplate mt, RecommendationMapper recommendationMapper) {
        return new RecommendationMongoRepoImpl(mt, recommendationMapper);
    }
}
