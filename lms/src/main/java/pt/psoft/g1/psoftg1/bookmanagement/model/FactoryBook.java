package pt.psoft.g1.psoftg1.bookmanagement.model;

import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;

public class FactoryBook {

    FactoryGenre _factoryGenre;

    public FactoryBook(FactoryGenre factoryGenre) {
        _factoryGenre = factoryGenre;
    }
}
