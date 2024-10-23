package pt.psoft.g1.psoftg1.shared.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.ForbiddenNameJpaRepoImpl;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.ForbiddenNameMongoRepoImpl;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;

@Configuration
public class ForbiddenNameRepositoryConfig {

    @Bean
    @Profile("jpa")
    public ForbiddenNameRepository jpaForbiddenNameRepository(EntityManager em) {
        return new ForbiddenNameJpaRepoImpl(em);
    }

    @Bean
    @Profile("mongo")
    public ForbiddenNameRepository mongoForbiddenNameRepository(MongoTemplate mt) {
        return new ForbiddenNameMongoRepoImpl(mt);
    }
}
