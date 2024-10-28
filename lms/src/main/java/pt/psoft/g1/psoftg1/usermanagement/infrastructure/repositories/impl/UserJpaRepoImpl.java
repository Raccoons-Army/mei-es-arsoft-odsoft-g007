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
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserJpaRepoImpl implements UserRepository {

    private final EntityManager em;


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
            @CacheEvict(value = "users", key = "#p0.id", condition = "#p0.id != null"),
            @CacheEvict(value = "users", key = "#p0.username", condition = "#p0.username != null")
    })
    public <S extends User> S save(S entity) {
        if (entity.getPk() != null) {
            // if ID is not null, update (merge)
            return em.merge(entity);
        } else {
            // otherwise, persist new entity
            em.persist(entity);
            return entity;
        }
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findById(Long objectId) {
        User user = em.find(User.class, objectId);
        return Optional.ofNullable(user);
    }

    @Override
    @Cacheable(value = "users")
    public Optional<User> findByUsername(String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root)
                .where(cb.equal(root.get("username"), username));

        return em.createQuery(query)
                .getResultStream()
                .findFirst();
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
