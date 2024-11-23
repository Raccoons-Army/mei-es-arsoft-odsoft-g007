package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.MongoFineModel;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.FineMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FineMongoRepoImpl implements FineRepository {

    private final MongoTemplate mt;
    private final FineMapper fineMapper;

    @Override
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("lending.lendingNumber").is(lendingNumber));
        Optional<MongoFineModel> m = Optional.ofNullable(mt.findOne(query, MongoFineModel.class));
        return m.map(fineMapper::fromMongoFineModel);
    }

    @Override
    public Iterable<Fine> findAll() {
        List<MongoFineModel> list = mt.findAll(MongoFineModel.class);
        return fineMapper.fromMongoFineModel(list);
    }

    @Override
    public Fine save(Fine fine) {
        MongoFineModel mongoGenre = fineMapper.toMongoFineModel(fine);
        MongoFineModel savedGenre = mt.save(mongoGenre);

        return fineMapper.fromMongoFineModel(savedGenre);
    }
}
