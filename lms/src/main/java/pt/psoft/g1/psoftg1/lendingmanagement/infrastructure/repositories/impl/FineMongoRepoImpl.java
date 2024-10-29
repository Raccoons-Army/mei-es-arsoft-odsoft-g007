package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.FineMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class FineMongoRepoImpl implements FineRepository {

    private final MongoTemplate mt;
    private final FineMapper fineMapper;

    @Override
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("lending.lendingNumber.lendingNumber").is(lendingNumber));
        return Optional.ofNullable(mt.findOne(query, Fine.class));
    }

    @Override
    public Iterable<Fine> findAll() {
        return mt.findAll(Fine.class);
    }

    @Override
    public Fine save(Fine fine) {
        return mt.save(fine);
    }
}
