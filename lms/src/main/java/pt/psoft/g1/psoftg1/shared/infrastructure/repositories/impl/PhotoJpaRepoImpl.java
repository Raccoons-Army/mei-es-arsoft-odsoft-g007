package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.dbSchema.JpaPhotoDTO;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;

public class PhotoJpaRepoImpl implements PhotoRepository {

    private final EntityManager em;

    public PhotoJpaRepoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Photo save(Photo entity) {
        if (entity.getPk() == 0) {
            em.persist(entity);
            return entity;
        } else {
            // check version
            JpaPhotoDTO photo = em.find(JpaPhotoDTO.class, entity.getPk());
            return em.merge(entity);
        }
    }

    @Override
    public void delete(Photo entity) {
        em.remove(entity);
    }

    @Override
    public List<Photo> findAll() {
        return em.createQuery("SELECT b FROM Photo b", Photo.class).getResultList();
    }

    @Override
    public Photo findById(Long aLong) {
        return em.find(Photo.class, aLong);
    }

    @Override
    public void deleteByPhotoFile(String photoFile) {
        String query = "DELETE FROM Photo p WHERE p.photoFile = :photoFile";
        em.createQuery(query)
                .setParameter("photoFile", photoFile)
                .executeUpdate();
    }

}
