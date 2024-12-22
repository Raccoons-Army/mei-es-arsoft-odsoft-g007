package pt.psoft.g1.psoftg1.genremanagement.services;

import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQP;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

public interface GenreService {
    Genre create(GenreViewAMQP genre);
    Genre update(GenreViewAMQP genre);
}
