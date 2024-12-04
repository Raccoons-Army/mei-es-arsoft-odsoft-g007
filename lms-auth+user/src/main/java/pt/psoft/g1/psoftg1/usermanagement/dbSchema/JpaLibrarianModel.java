package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
public class JpaLibrarianModel extends JpaUserModel {

    protected JpaLibrarianModel() {
        // got ORM only
    }

    public JpaLibrarianModel(String username, String password) {
        super(username, password);
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
    public static JpaLibrarianModel newLibrarian(final String username, final String password, final String name) {
        final var u = new JpaLibrarianModel(username, password);
        u.setName(name);
        return u;
    }
}
