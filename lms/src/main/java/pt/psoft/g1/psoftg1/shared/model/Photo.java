package pt.psoft.g1.psoftg1.shared.model;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

public class Photo {

    @Getter
    private long id;

    @Getter
    @Setter
    private String photoFile;

    protected Photo (){}

    public Photo (Path photoPath){
        setPhotoFile(photoPath.toString());
    }
}

