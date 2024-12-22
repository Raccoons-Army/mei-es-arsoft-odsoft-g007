package pt.psoft.g1.psoftg1.authormanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public Optional<Author> findByAuthorNumber(final String authorNumber) {
        return authorRepository.findByAuthorNumber(authorNumber);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorRepository.searchByNameNameStartsWith(name);
    }

    @Override
    public Author create(AuthorViewAMQP resource) {
        // check if already exists
        if (authorRepository.findByAuthorNumber(resource.getAuthorNumber()).isPresent()) {
            throw new ConflictException("Author with number " + resource.getAuthorNumber() + " already exists");
        }
        final Author author = new Author(resource.getAuthorNumber(), resource.getName());
        return authorRepository.save(author);
    }

    @Override
    public Author update(AuthorViewAMQP resource) {
        final Author author = authorRepository.findByAuthorNumber(resource.getAuthorNumber())
                .orElseThrow(() -> new NotFoundException("Cannot find author"));
        author.setName(resource.getName());
        return authorRepository.save(author);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        return authorRepository.findCoAuthorsByAuthorNumber(authorNumber);
    }
}

