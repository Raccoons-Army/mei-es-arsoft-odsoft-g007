package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoUserModel;
import pt.psoft.g1.psoftg1.usermanagement.mapper.UserMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserMongoRepoImpl implements UserRepository {

    private final MongoTemplate mt;
    private final UserMapper userMapper;

    @Override
    public <S extends User> User save(S entity) {
        MongoUserModel mongoUserModel = userMapper.toMongoUserModel(entity);
        MongoUserModel savedUserModel = mt.save(mongoUserModel);

        return userMapper.fromMongoUserModel(savedUserModel);
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findById(String userId) {
        MongoUserModel mongoUserModel = mt.findById(userId, MongoUserModel.class);

        return Optional.ofNullable(mongoUserModel)
                .map(userMapper::fromMongoUserModel);
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        MongoUserModel mongoUserModel = mt.findOne(query, MongoUserModel.class);

        if (mongoUserModel == null) {
            return Optional.empty();
        }

        return Optional.of(userMapper.fromMongoUserModel(mongoUserModel));
    }


    @Override
    public void delete(User user) {
        mt.remove(user);
    }
}
