package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;

public class PhotoMongoRepoImpl implements PhotoRepository {

    public MongoTemplate mongoTemplate;

    public PhotoMongoRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Photo save(Photo entity) {
        return mongoTemplate.save(entity);
    }

    @Override
    public void delete(Photo entity) {
        mongoTemplate.remove(entity);
    }

    @Override
    public List<Photo> findAll() {
        return mongoTemplate.findAll(Photo.class);
    }

    @Override
    public Photo findById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Photo.class);
    }

    @Override
    public void deleteByPhotoFile(String photoFile) {
        Query query = new Query();
        query.addCriteria(Criteria.where("photoFile").is(photoFile));
        mongoTemplate.remove(query, Photo.class);
    }
}
