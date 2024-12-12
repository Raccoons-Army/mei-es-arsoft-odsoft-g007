package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.shared.dbSchema.JpaForbiddenNameModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoForbiddenNameModel;
import pt.psoft.g1.psoftg1.shared.mapper.ForbiddenNameMapper;
import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ForbiddenNameMongoRepoImpl implements ForbiddenNameRepository {

    private final MongoTemplate mt;
    private final ForbiddenNameMapper forbiddenNameMapper;

    @Override
    public Iterable<ForbiddenName> findAll() {
        List<MongoForbiddenNameModel> mongoForbiddenNames = mt.findAll(MongoForbiddenNameModel.class);
        return forbiddenNameMapper.fromMongoForbiddenNameModel(mongoForbiddenNames);
    }

    @Override
    public List<ForbiddenName> findByForbiddenNameIsContained(String pat) {
        Query query = new Query();
        query.addCriteria(Criteria.where("forbiddenName").regex(pat, "i"));
        List<MongoForbiddenNameModel> mongoForbiddenNames = mt.find(query, MongoForbiddenNameModel.class);

        return forbiddenNameMapper.fromMongoForbiddenNameModel(mongoForbiddenNames);
    }

    @Override
    public ForbiddenName save(ForbiddenName forbiddenName) {
        MongoForbiddenNameModel mongoForbiddenName = forbiddenNameMapper.toMongoForbiddenNameModel(forbiddenName);
        MongoForbiddenNameModel savedForbiddenName = mt.save(mongoForbiddenName);

        return forbiddenNameMapper.fromMongoForbiddenNameModel(savedForbiddenName);
    }

    @Override
    public Optional<ForbiddenName> findByForbiddenName(String forbiddenName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("forbiddenName").is(forbiddenName));
        MongoForbiddenNameModel mongoForbiddenName = mt.findOne(query, MongoForbiddenNameModel.class);

        return Optional.ofNullable(mongoForbiddenName)
                .map(forbiddenNameMapper::fromMongoForbiddenNameModel);
    }

    @Override
    public int deleteForbiddenName(String forbiddenName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("forbiddenName").is(forbiddenName));

        return (int) mt.remove(query, MongoForbiddenNameModel.class).getDeletedCount();
    }
}
