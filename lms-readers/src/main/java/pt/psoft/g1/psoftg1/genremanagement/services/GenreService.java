package pt.psoft.g1.psoftg1.genremanagement.services;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.Optional;

public interface GenreService {
    Iterable<Genre> findAll();
    Genre create(String genre);
    Optional<Genre> findByString(String name);
}
