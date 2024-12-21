package pt.psoft.g1.psoftg1.authormanagement.model;

import org.springframework.stereotype.Component;

@Component
public class FactoryAuthor {

    public Author newAuthor(String authorNumber) throws InstantiationException {
        return new Author(authorNumber);
    }

    public Author newAuthor(String authorNumber, long version) {
        return new Author(authorNumber, version);
    }
}
