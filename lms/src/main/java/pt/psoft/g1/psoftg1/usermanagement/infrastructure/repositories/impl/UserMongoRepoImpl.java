package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserMongoRepoImpl implements UserRepository {

    private final MongoTemplate mt;

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(mt.save(entity));
        }
        return result;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#p0.id", condition = "#p0.id != null"),
            @CacheEvict(value = "users", key = "#p0.username", condition = "#p0.username != null")
    })
    public <S extends User> S save(S entity) {
        return mt.save(entity);
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findById(Long objectId) {
        User user = mt.findById(objectId, User.class);
        return Optional.ofNullable(user);
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return Optional.ofNullable(mt.findOne(query, User.class));
    }

    @Override
    public List<User> searchUsers(Page page, SearchUsersQuery query) {
        Query mongoQuery = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (query.getUsername() != null && !query.getUsername().isEmpty()) {
            criteriaList.add(Criteria.where("username").is(query.getUsername()));
        }
        if (query.getFullName() != null && !query.getFullName().isEmpty()) {
            criteriaList.add(Criteria.where("fullName").regex(".*" + query.getFullName() + ".*", "i"));
        }

        if (!criteriaList.isEmpty()) {
            mongoQuery.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }

        // Apply default sorting if needed
        mongoQuery.with(Sort.by(Sort.Direction.DESC, "createdAt"));

        // Apply pagination based on the custom Page object
        mongoQuery.skip((long) (page.getNumber() - 1) * page.getLimit()).limit(page.getLimit());

        // Execute the query
        List<User> users = mt.find(mongoQuery, User.class);
        long total = mt.count(mongoQuery, User.class);

        return new PageImpl<>(users, org.springframework.data.domain.PageRequest.of(page.getNumber() - 1, page.getLimit(), Sort.by(Sort.Direction.DESC, "createdAt")), total).getContent();
    }

    @Override
    @Cacheable(value = "users")
    public List<User> findByNameName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mt.find(query, User.class);
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        String pattern = ".*" + name + ".*";
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(pattern, "i"));
        return mt.find(query, User.class);
    }

    @Override
    public void delete(User user) {
        mt.remove(user);
    }
}
