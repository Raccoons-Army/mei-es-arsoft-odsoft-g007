package pt.psoft.g1.psoftg1.shared.config;

import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.ForbiddenNameJpaRepoImpl;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.ForbiddenNameMongoRepoImpl;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;

@Configuration
public class ForbiddenNameRepositoryConfig {

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "jpa")
    public ForbiddenNameRepository jpaForbiddenNameRepository(EntityManager em) {
        return new ForbiddenNameJpaRepoImpl(em);
    }

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "mongodb")
    public ForbiddenNameRepository mongoForbiddenNameRepository(MongoTemplate mt) {
        return new ForbiddenNameMongoRepoImpl(mt);
    }
}
