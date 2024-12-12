package pt.psoft.g1.psoftg1.readermanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.ReaderJpaRepoImpl;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.ReaderMongoRepoImpl;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

@Configuration
public class ReaderRepositoryConfig {

    @Bean
    @Profile("jpa")
    public ReaderRepository jpaReaderRepository(EntityManager em, ReaderDetailsMapper readerDetailsMapper) {
        return new ReaderJpaRepoImpl(em, readerDetailsMapper);
    }

    @Bean
    @Profile("mongo")
    public ReaderRepository mongoReaderRepository(MongoTemplate mt, ReaderDetailsMapper readerDetailsMapper) {
        return new ReaderMongoRepoImpl(mt, readerDetailsMapper);
    }
}
