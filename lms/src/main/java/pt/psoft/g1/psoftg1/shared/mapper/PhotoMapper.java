package pt.psoft.g1.psoftg1.shared.mapper;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.shared.dbSchema.JpaPhotoModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoPhotoModel;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.nio.file.Path;

@Mapper(componentModel = "spring")
public abstract class PhotoMapper {

    // Custom mapping method for MongoPhotoModel to String
    public String mapMongoPhotoModelToString(MongoPhotoModel photo) {
        return (photo != null) ? photo.getPhotoFile() : null;
    }

    // Custom mapping method for String to MongoPhotoModel
    public MongoPhotoModel mapStringToMongoPhotoModel(String photoUri) {
        return (photoUri != null) ? new MongoPhotoModel(photoUri) : null;
    }

    // Custom mapping method for JpaPhotoModel to String
    public String mapJpaPhotoModelToString(JpaPhotoModel photo) {
        return (photo != null) ? photo.getPhotoFile() : null;
    }

    // Custom mapping method for String to JpaPhotoModel
    public JpaPhotoModel mapStringToJpaPhotoModel(String photoUri) {
        return (photoUri != null) ? new JpaPhotoModel(photoUri) : null;
    }

    // Custom mapping method for Photo to String
    public String mapPhotoToString(Photo photo) {
        return (photo != null) ? photo.getPhotoFile() : null;
    }
}
