package pt.psoft.g1.psoftg1.usermanagement.model;

import org.springframework.stereotype.Component;

@Component
public class FactoryUser {

    public Reader newReader(String username, String password, String name) {
        return Reader.newReader(username, password, name);
    }

    public Reader newReader(String username, String name) {
        return Reader.newReader(username, name);
    }

    public Librarian newLibrarian(String username, String password, String name) {
        return Librarian.newLibrarian(username, password, name);
    }
}
