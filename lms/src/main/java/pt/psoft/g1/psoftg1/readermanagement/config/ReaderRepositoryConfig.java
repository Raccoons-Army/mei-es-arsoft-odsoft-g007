package pt.psoft.g1.psoftg1.readermanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.ReaderJpaRepoImpl;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.ReaderMongoRepoImpl;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

@Configuration
public class ReaderRepositoryConfig {

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "jpa")
    public ReaderRepository jpaReaderRepository(EntityManager em) {
        return new ReaderJpaRepoImpl(em);
    }

    @Bean
    @ConditionalOnProperty(name = "app.persistence.strategy", havingValue = "mongodb")
    public ReaderRepository mongoReaderRepository(MongoTemplate mt) {
        return new ReaderMongoRepoImpl(mt);
    }
}
