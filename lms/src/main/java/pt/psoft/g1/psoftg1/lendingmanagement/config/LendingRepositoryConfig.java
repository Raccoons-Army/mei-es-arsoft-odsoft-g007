package pt.psoft.g1.psoftg1.lendingmanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.LendingJpaRepoImpl;
import pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.LendingMongoRepoImpl;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;

@Configuration
public class LendingRepositoryConfig {

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "jpa")
    public LendingRepository jpaLendingRepository(EntityManager em) {
        return new LendingJpaRepoImpl(em);
    }

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "mongodb")
    public LendingRepository mongoLendingRepository(MongoTemplate mongoTemplate) {
        return new LendingMongoRepoImpl(mongoTemplate);
    }
}
