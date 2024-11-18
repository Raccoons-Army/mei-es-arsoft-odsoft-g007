package pt.psoft.g1.psoftg1.usermanagement.model;

public class Reader extends User {

    public Reader(String username) {
        super(username);
        this.addAuthority(new Role(Role.READER));
    }

    public Reader() {
        // for ORM only
    }

    // for mapstruct
    public static Reader newReader(final String username) {
        return new Reader(username);
    }

    public static Reader newReader(final String pk, final String username) {
        final var u = new Reader(username);
        u.setPk(pk);
        return u;
    }
}
