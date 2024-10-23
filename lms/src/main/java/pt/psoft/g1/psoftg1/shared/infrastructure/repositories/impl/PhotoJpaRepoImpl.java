package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

@RequiredArgsConstructor
public class PhotoJpaRepoImpl implements PhotoRepository {

    private final EntityManager em;

    @Override
    public void deleteByPhotoFile(String photoFile) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Photo> delete = cb.createCriteriaDelete(Photo.class);
        Root<Photo> root = delete.from(Photo.class);

        delete.where(cb.equal(root.get("photoFile"), photoFile));

        em.createQuery(delete).executeUpdate();
    }
}
