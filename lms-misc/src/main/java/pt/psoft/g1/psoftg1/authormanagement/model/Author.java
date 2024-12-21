package pt.psoft.g1.psoftg1.authormanagement.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;

@Setter
public class Author {

    @Getter
    private String authorNumber;

    private long version;


    public Long getVersion() {
        return version;
    }

    public String getPk() {
        return authorNumber;
    }

    public Author(String authorNumber) {
        this.authorNumber = authorNumber;
    }

    // for factory
    public Author(String authorNumber, long version) {
        this.authorNumber = authorNumber;
        this.version = version;
    }

    // for mapper
    public Author() {
    }


    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (this.version != desiredVersion)
            throw new StaleObjectStateException("Object was already modified by another user", this.authorNumber);
    }
}

