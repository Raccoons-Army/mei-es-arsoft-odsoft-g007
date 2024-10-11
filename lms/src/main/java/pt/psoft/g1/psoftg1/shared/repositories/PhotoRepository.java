package pt.psoft.g1.psoftg1.shared.repositories;

import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import java.util.Optional;


public interface PhotoRepository extends CRUDRepository<Photo, Long> {
    void deleteByPhotoFile(String photoFile);
}
