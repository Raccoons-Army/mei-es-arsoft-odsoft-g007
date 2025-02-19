package pt.psoft.g1.psoftg1.shared.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.PhotoJpaRepoImpl;
import pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.PhotoMongoRepoImpl;
import pt.psoft.g1.psoftg1.shared.mapper.PhotoMapper;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

@Configuration
public class PhotoRepositoryConfig {

    @Bean
    @Profile("jpa")
    public PhotoRepository jpaPhotoRepository(EntityManager em, PhotoMapper photoMapper) {
        return new PhotoJpaRepoImpl(em, photoMapper);
    }

    @Bean
    @Profile("mongo")
    public PhotoRepository mongoPhotoRepository(MongoTemplate mt, PhotoMapper photoMapper) {
        return new PhotoMongoRepoImpl(mt, photoMapper);
    }
}
