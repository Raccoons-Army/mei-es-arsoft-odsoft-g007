package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;

@Component
public class FactoryBook {

    FactoryGenre _factoryGenre;
    FactoryAuthor _factoryAuthor;

    public FactoryBook(FactoryGenre factoryGenre, FactoryAuthor factoryAuthor) {
        _factoryGenre = factoryGenre;
        _factoryAuthor = factoryAuthor;
    }
}
