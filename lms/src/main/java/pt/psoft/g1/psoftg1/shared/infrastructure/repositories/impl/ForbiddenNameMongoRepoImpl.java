package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.shared.mapper.ForbiddenNameMapper;
import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ForbiddenNameMongoRepoImpl implements ForbiddenNameRepository {

    private final MongoTemplate mt;
    private final ForbiddenNameMapper forbiddenNameMapper;

    @Override
    public Iterable<ForbiddenName> findAll() {
        return null;
    }

    @Override
    public List<ForbiddenName> findByForbiddenNameIsContained(String pat) {
        return List.of();
    }

    @Override
    public ForbiddenName save(ForbiddenName forbiddenName) {
        return null;
    }

    @Override
    public Optional<ForbiddenName> findByForbiddenName(String forbiddenName) {
        return Optional.empty();
    }

    @Override
    public int deleteForbiddenName(String forbiddenName) {
        return 0;
    }
}
