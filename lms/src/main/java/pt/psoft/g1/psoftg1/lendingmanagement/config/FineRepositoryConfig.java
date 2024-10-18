package pt.psoft.g1.psoftg1.lendingmanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.FineJpaRepoImpl;
import pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.FineMongoRepoImpl;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

@Configuration
public class FineRepositoryConfig {

    @Bean
    @Profile("jpa")
    public FineRepository jpaFineRepository(EntityManager em) {
        return new FineJpaRepoImpl(em);
    }

    @Bean
    @Profile("mongo")
    public FineRepository mongoFineRepository(MongoTemplate mt) {
        return new FineMongoRepoImpl(mt);
    }
}
