package pt.psoft.g1.psoftg1.lendingmanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.LendingJpaRepoImpl;
import pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.LendingMongoRepoImpl;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.LendingMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;

@Configuration
public class LendingRepositoryConfig {

    @Bean
    @Profile("jpa")
    public LendingRepository jpaLendingRepository(EntityManager em, LendingMapper lendingMapper) {
        return new LendingJpaRepoImpl(em, lendingMapper);
    }

    @Bean
    @Profile("mongo")
    public LendingRepository mongoLendingRepository(MongoTemplate mt) {
        return new LendingMongoRepoImpl(mt);
    }
}
