package pt.psoft.g1.psoftg1.usermanagement.model;

public class Reader extends User {

    public Reader(String username, String password) {
        super(username, password);
        this.addAuthority(new Role(Role.READER));
    }

    public Reader(String username) {
        super(username);
        this.addAuthority(new Role(Role.READER));
    }

    public Reader() {
        // for ORM only
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

    public static Reader newReader(final String username, final String password, final String name) {
        final var u = new Reader(username, password);
        u.setName(name);
        return u;
    }

    public static Reader newReader(final String pk, final String username, final String password, final String name, final long version) {
        final var u = new Reader(username, password);
        u.setName(name);
        u.setPk(pk);
        u.setVersion(version);
        return u;
    }

    public static Reader newReader(final String username, final String name) {
        final var u = new Reader(username);
        u.setName(name);
        return u;
    }
}
