package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaUserModel;
import pt.psoft.g1.psoftg1.usermanagement.mapper.UserMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserJpaRepoImpl implements UserRepository {

    private final EntityManager em;
    private final UserMapper userMapper;

    @Override
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
    public Optional<User> findById(String userId) {
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
    public void delete(User user) {
        JpaUserModel jpaUser = userMapper.toJpaUserModel(user);
        if (em.contains(jpaUser)) {
            em.remove(jpaUser);  // If the entity is already managed, directly remove it
        } else {
            JpaUserModel managedUser = em.merge(jpaUser);  // Merge the detached entity to get it managed
            em.remove(managedUser);  // Then remove it
        }
    }
}
