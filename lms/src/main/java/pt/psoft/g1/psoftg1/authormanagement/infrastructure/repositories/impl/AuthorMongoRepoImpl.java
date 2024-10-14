package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AuthorMongoRepoImpl implements AuthorRepository {

    public MongoTemplate mongoTemplate;

    public AuthorMongoRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        return Optional.empty();
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        return null;
    }

    @Override
    public List<Author> searchByNameName(String name) {
        return null;
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        // Define the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("lending", "id", "bookId", "lendings"), // Lookup Lending documents
                Aggregation.unwind("lendings"), // Unwind the lendings array
                Aggregation.group("id")
                        .first("name").as("name")
                        .count().as("lendingCount"), // Count lendings per author
                Aggregation.sort(Aggregation.sort().descending("lendingCount")),
                Aggregation.skip(pageableRules.getOffset()), // Apply pagination offset
                Aggregation.limit(pageableRules.getPageSize()) // Apply limit based on page size
        );

        // Execute the aggregation
        AggregationResults<AuthorLendingView> results = mongoTemplate.aggregate(aggregation, "author", AuthorLendingView.class);
        List<AuthorLendingView> topAuthors = results.getMappedResults();

        // Total count for pagination (needs a separate aggregation to count total results)
        long total = mongoTemplate.count(new Query(), "author");

        return new PageImpl<>(topAuthors, pageableRules, total);
    }


    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        // Find all books that include the given author number
        Query query = new Query();
        query.addCriteria(Criteria.where("authorIds").is(authorNumber));
        List<Book> books = mongoTemplate.find(query, Book.class);

        // Extract the author IDs from these books, excluding the provided authorNumber
        List<Long> coAuthorIds = books.stream()
                .flatMap(book -> book.getAuthors().stream())
                .distinct()
                .filter(id -> !id.equals(authorNumber))
                .toList();

        // Query authors based on these co-author IDs
        Query coAuthorQuery = new Query();
        coAuthorQuery.addCriteria(Criteria.where("id").in(coAuthorIds));

        return mongoTemplate.find(coAuthorQuery, Author.class);
    }

    @Override
    public Author save(Author entity) {
        return mongoTemplate.save(entity);
    }

    @Override
    public void delete(Author entity) {
        mongoTemplate.remove(entity);
    }

    @Override
    public List<Author> findAll() {
        return mongoTemplate.findAll(Author.class);
    }

    @Override
    public Optional<Author> findById(Long aLong) {
        return Optional.ofNullable(mongoTemplate.findById(aLong, Author.class));
    }
}
