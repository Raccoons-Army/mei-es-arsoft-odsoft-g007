package pt.psoft.g1.psoftg1.suggestedbookmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@Table(name = " SuggestedBook")
public class JpaSuggestedBookModel {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String pk;

    @Version
    private Long version;

    @Size(min = 10, max = 13)
    @Column(name = "ISBN", length = 16)
    private String isbn;

    public JpaSuggestedBookModel(String pk, Long version, String isbn) {
        this.pk = pk;
        this.version = version;
        this.isbn = isbn;
    }

    protected JpaSuggestedBookModel() {}
}
