package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class FineMongoRepoImpl implements FineRepository {

    public final MongoTemplate mt;

    @Override
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        return Optional.empty();
    }

    @Override
    public Iterable<Fine> findAll() {
        return null;
    }

    @Override
    public Fine save(Fine fine) {
        return null;
    }
}
