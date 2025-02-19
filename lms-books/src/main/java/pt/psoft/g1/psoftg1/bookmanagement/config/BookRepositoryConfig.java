package pt.psoft.g1.psoftg1.bookmanagement.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.BookJpaRepoImpl;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.BookMongoRepoImpl;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;

@Configuration
public class BookRepositoryConfig {

    @Bean
    @Profile("jpa")
    public BookRepository jpaBookRepository(EntityManager em, BookMapper bookMapper) {
        return new BookJpaRepoImpl(em, bookMapper);
    }

    @Bean
    @Profile("mongo")
    public BookRepository mongoBookRepository(MongoTemplate mt, BookMapper bookMapper) {
        return new BookMongoRepoImpl(mt, bookMapper);
    }
}
