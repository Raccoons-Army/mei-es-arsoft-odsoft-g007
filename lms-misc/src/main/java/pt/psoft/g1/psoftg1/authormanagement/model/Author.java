package pt.psoft.g1.psoftg1.authormanagement.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.shared.model.Name;

public class Author {

    @Setter
    @Getter
    private String authorNumber;

    @Setter
    private long version;

    private Name name;

    public void setName(String name) {
        this.name = new Name(name);
    }

    public Long getVersion() {
        return version;
    }

    public String getPk() {
        return authorNumber;
    }

    public Author(String authorNumber, String name) {
        this.authorNumber = authorNumber;
        setName(name);
    }

    // for factory
    public Author(String authorNumber, String name, long version) {
        this.authorNumber = authorNumber;
        setName(name);
        this.version = version;
    }

    // for mapper
    public Author() {
    }


    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (this.version != desiredVersion)
            throw new StaleObjectStateException("Object was already modified by another user", this.authorNumber);
        if (request.getName() != null)
            setName(request.getName());
    }

    public String getName() {
        return this.name.toString();
    }
}

