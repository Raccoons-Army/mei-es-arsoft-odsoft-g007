package pt.psoft.g1.psoftg1.authormanagement.services;

import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Optional<Author> findByAuthorNumber(String authorNumber);
    List<Author> findByName(String name);
    Author create(AuthorViewAMQP resource);
    Author update(AuthorViewAMQP resource);
    List<Author> findCoAuthorsByAuthorNumber(String authorNumber);
}
