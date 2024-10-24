package pt.psoft.g1.psoftg1.genremanagement.model;

import org.springframework.stereotype.Component;

@Component
public class FactoryGenre {

    public Genre newGenre(String name) throws InstantiationException {
        return new Genre(name);
    }
}
