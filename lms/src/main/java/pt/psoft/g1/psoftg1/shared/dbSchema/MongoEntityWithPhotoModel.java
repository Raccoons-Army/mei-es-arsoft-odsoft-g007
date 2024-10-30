package pt.psoft.g1.psoftg1.shared.dbSchema;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.lang.Nullable;

@Getter
@Setter
public abstract class MongoEntityWithPhotoModel {

    @Nullable
    @DBRef
    protected MongoPhotoModel photo;

    //This method is used by the mapper in order to set the photo. This will call the setPhotoInternal method that
    //will contain all the logic to set the photo
    public void setThePhoto(String photoUri) {
        this.setPhotoInternal(photoUri);
    }

    protected void setPhotoInternal(String photoURI) {
        if (photoURI == null) {
            this.photo = null;
        } else {
            this.photo = new MongoPhotoModel(photoURI);
        }
    }
}
