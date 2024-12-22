package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.MongoLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.LendingMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LendingMongoRepoImpl implements LendingRepository {

    private final MongoTemplate mt;
    private final LendingMapper lendingMapper;

    @Override
    public Lending save(Lending lending) {
        MongoLendingModel mongoLending = lendingMapper.toMongoLendingModel(lending);
        MongoLendingModel savedLending = mt.save(mongoLending);

        return lendingMapper.fromMongoLendingModel(savedLending);
    }

    @Override
    public void delete(Lending lending) {
        MongoLendingModel mongoLending = lendingMapper.toMongoLendingModel(lending);

        mt.remove(mongoLending);
    }

    @Override
    public List<Lending> findAll() {
        List<MongoLendingModel> mongoLendings = mt.findAll(MongoLendingModel.class);

        return lendingMapper.fromMongoLendingModel(mongoLendings);
    }

    @Override
    public Optional<Lending> findById(Long id) {
        MongoLendingModel mongoLending = mt.findById(id, MongoLendingModel.class);

        return Optional.ofNullable(mongoLending)
                .map(lendingMapper::fromMongoLendingModel);
    }

    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("lendingNumber").is(lendingNumber));
        MongoLendingModel mongoLending = mt.findOne(query, MongoLendingModel.class);

        return Optional.ofNullable(mongoLending)
                .map(lendingMapper::fromMongoLendingModel);
    }
}
