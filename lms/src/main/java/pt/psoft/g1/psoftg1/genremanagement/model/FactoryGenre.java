package pt.psoft.g1.psoftg1.genremanagement.model;

public class FactoryGenre {

    public FactoryGenre() {}

    public Genre newGenre(String name) {
        return new Genre(name);
    }
}
