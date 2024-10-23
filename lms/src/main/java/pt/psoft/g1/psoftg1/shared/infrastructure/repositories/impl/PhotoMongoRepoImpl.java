package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

@RequiredArgsConstructor
public class PhotoMongoRepoImpl implements PhotoRepository {

    private final MongoTemplate mt;

    @Override
    public void deleteByPhotoFile(String photoFile) {

    }
}
