package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

@Getter
@Setter
@Entity
public class JpaReaderModel extends JpaUserModel{

    protected JpaReaderModel() {
        // got ORM only
    }

    public JpaReaderModel(String username, String password) {
        super(username, password);
        this.addAuthority(new Role(Role.READER));
    }

    /**
     * factory method. since mapstruct does not handle protected/private setters
     * neither more than one public constructor, we use these factory methods for
     * helper creation scenarios
     *
     * @param username
     * @param password
     * @param name
     * @return
     */

    public static JpaReaderModel newReader(final String username, final String password, final String name) {
        final var u = new JpaReaderModel(username, password);
        u.setName(name);
        return u;
    }
}
