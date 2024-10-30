package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoForbiddenNameModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoPhotoModel;
import pt.psoft.g1.psoftg1.shared.mapper.PhotoMapper;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PhotoMongoRepoImpl implements PhotoRepository {

    private final MongoTemplate mt;
    private final PhotoMapper photoMapper;

    @Override
    public void deleteByPhotoFile(String photoFile) {

    }

    @Override
    public Photo save(Photo photo) {
        MongoPhotoModel mongoPhoto = photoMapper.mapStringToMongoPhotoModel(photo.getPhotoFile());
        MongoPhotoModel savedPhoto = mt.save(mongoPhoto);

        return photoMapper.mapMongoPhotoModelToPhoto(savedPhoto);
    }

    @Override
    public void delete(Photo entity) {

    }

    @Override
    public List<Photo> findAll() {
        return null;
    }

    @Override
    public Optional<Photo> findById(Long aLong) {
        return Optional.empty();
    }
}
