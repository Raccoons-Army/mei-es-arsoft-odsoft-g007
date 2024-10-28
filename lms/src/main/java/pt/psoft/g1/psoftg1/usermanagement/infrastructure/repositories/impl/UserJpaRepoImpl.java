package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaUserModel;
import pt.psoft.g1.psoftg1.usermanagement.mapper.UserMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserJpaRepoImpl implements UserRepository {

    private final EntityManager em;
    private final UserMapper userMapper;


    @Override
    @CacheEvict(value = "users", allEntries = true)
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(em.merge(entity));
        }
        em.flush(); // ensure all entities are saved to the database
        return result;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#p0.pk", condition = "#p0.pk != null"),
            @CacheEvict(value = "users", key = "#p0.username", condition = "#p0.username != null")
    })
    public <S extends User> User save(S user) {
        JpaUserModel jpaUser = userMapper.toJpaUserModel(user);
        if (user.getPk() == null) {
            em.persist(jpaUser);
        } else {
            em.merge(jpaUser);
        }
        return userMapper.fromJpaUserModel(jpaUser);
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findById(Long userId) {
        Optional<JpaUserModel> jpaUser = Optional.ofNullable(em.find(JpaUserModel.class, userId));
        return jpaUser.map(userMapper::fromJpaUserModel);
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findByUsername(String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaUserModel> query = cb.createQuery(JpaUserModel.class);
        Root<JpaUserModel> root = query.from(JpaUserModel.class);

        query.select(root)
                .where(cb.equal(root.get("username"), username));

        Optional<JpaUserModel> jpaUser = em.createQuery(query)
                .getResultStream()
                .findFirst();

        return jpaUser.map(userMapper::fromJpaUserModel);
    }

    @Override
    public List<User> searchUsers(Page page, SearchUsersQuery query) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<User> cq = cb.createQuery(User.class);
        final Root<User> root = cq.from(User.class);
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();
        if (StringUtils.hasText(query.getUsername())) {
            where.add(cb.equal(root.get("username"), query.getUsername()));
        }
        if (StringUtils.hasText(query.getFullName())) {
            where.add(cb.like(root.get("fullName"), "%" + query.getFullName() + "%"));
        }

        // search using OR
        if (!where.isEmpty()) {
            cq.where(cb.or(where.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.desc(root.get("createdAt")));

        final TypedQuery<User> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }

    @Override
    @Cacheable(value = "users")
    public List<User> findByNameName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root)
                .where(cb.equal(root.get("name"), name));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        String pattern = "%" + name + "%";
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root)
                .where(cb.like(root.get("name"), pattern));

        return em.createQuery(query).getResultList();
    }

    @Override
    public void delete(User user) {
        if (em.contains(user)) {
            em.remove(user);  // If the entity is already managed, directly remove it
        } else {
            User managedUser = em.merge(user);  // Merge the detached entity to get it managed
            em.remove(managedUser);  // Then remove it
        }
    }
}
