package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.shared.dbSchema.JpaPhotoModel;
import pt.psoft.g1.psoftg1.shared.mapper.PhotoMapper;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PhotoJpaRepoImpl implements PhotoRepository {

    private final EntityManager em;
    private final PhotoMapper photoMapper;

    @Override
    public void deleteByPhotoFile(String photoFile) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<JpaPhotoModel> delete = cb.createCriteriaDelete(JpaPhotoModel.class);
        Root<JpaPhotoModel> root = delete.from(JpaPhotoModel.class);

        delete.where(cb.equal(root.get("photoFile"), photoFile));

        em.createQuery(delete).executeUpdate();
    }

    @Override
    public Photo save(Photo entity) {
        return null;
    }

    @Override
    public void delete(Photo entity) {

    }

    @Override
    public List<Photo> findAll() {
        return null;
    }

    @Override
    public Optional<Photo> findById(Long aLong) {
        return Optional.empty();
    }
}
