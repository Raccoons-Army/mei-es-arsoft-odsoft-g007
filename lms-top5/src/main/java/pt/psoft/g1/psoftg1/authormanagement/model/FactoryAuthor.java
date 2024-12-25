package pt.psoft.g1.psoftg1.authormanagement.model;

import org.springframework.stereotype.Component;

@Component
public class FactoryAuthor {

    public Author newAuthor(String authorNumber, String name) throws InstantiationException {
        return new Author(authorNumber, name);
    }

    public Author newAuthor(String authorNumber, String name, long version) {
        return new Author(authorNumber, name, version);
    }
}
