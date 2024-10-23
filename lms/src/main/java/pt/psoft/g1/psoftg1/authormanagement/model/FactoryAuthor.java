package pt.psoft.g1.psoftg1.authormanagement.model;

public class FactoryAuthor {

    public Author newAuthor(String authorNumber, String name, String bio, String photoURI) {
        return new Author(authorNumber, name, bio, photoURI);
    }
}
