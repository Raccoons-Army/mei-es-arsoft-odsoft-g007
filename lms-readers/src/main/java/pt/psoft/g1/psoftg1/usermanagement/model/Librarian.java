package pt.psoft.g1.psoftg1.usermanagement.model;

public class Librarian extends User {
    protected Librarian() {
        // for ORM only
    }
    public Librarian(String username) {
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

    public static Librarian newLibrarian(final String username) {
        final var u = new Librarian(username);
        u.addAuthority(new Role(Role.LIBRARIAN));
        return u;
    }
}
