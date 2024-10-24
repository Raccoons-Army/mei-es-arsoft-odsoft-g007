package pt.psoft.g1.psoftg1.authormanagement.model;

import org.springframework.stereotype.Component;

@Component
public class FactoryAuthor {

    public Author newAuthor(String authorNumber, String name, String bio, String photoURI) throws InstantiationException {
        return new Author(authorNumber, name, bio, photoURI);
    }
}
