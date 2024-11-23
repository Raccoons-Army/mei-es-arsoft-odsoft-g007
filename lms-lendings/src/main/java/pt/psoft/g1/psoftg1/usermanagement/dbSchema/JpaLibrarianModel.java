package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import jakarta.persistence.Entity;

@Entity
public class JpaLibrarianModel extends JpaUserModel {

    protected JpaLibrarianModel() {
        // got ORM only
    }

    public JpaLibrarianModel(String username) {
        super(username);
    }

    /**
     * factory method. since mapstruct does not handle protected/private setters
     * neither more than one public constructor, we use these factory methods for
     * helper creation scenarios
     *
     * @param username
     * @return
     */
    public static JpaLibrarianModel newLibrarian(final String username) {
        return new JpaLibrarianModel(username);
    }
}
