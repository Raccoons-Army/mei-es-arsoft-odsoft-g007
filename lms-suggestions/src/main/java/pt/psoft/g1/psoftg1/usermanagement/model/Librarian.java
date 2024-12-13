package pt.psoft.g1.psoftg1.usermanagement.model;

public class Librarian extends User {

    protected Librarian() {
        // for ORM only
    }
    public Librarian(String username) {
        super(username);
    }

    public static Librarian newLibrarian(final String username) {
        final var u = new Librarian(username);
        u.addAuthority(new Role(Role.LIBRARIAN));
        return u;
    }
}
