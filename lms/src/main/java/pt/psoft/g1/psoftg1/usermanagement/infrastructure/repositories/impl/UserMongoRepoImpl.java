package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserMongoRepoImpl implements UserRepository {
    public final MongoTemplate mt;

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#p0.id", condition = "#p0.id != null"),
            @CacheEvict(value = "users", key = "#p0.username", condition = "#p0.username != null")
    })
    public <S extends User> S save(S entity) {
        return null;
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findById(Long objectId) {
        return Optional.empty();
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> searchUsers(Page page, SearchUsersQuery query) {
        return List.of();
    }

    @Override
    @Cacheable(value = "users")
    public List<User> findByNameName(String name) {
        return List.of();
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        return List.of();
    }

    @Override
    public void delete(User user) {

    }
}
