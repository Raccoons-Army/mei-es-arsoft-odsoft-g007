package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoPhotoModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookMongoRepoImpl implements BookRepository {

    private final MongoTemplate mt;

    private final BookMapper bookMapper;

    @Override
    public List<Book> findByGenre(String genreName) {
        // Step 1: Define the Aggregation pipeline
        AggregationOperation lookupGenre = Aggregation.lookup("genres", "genre.$id", "_id", "genreDetails");

        // Step 2: Match the genre by name in the genreDetails field (after lookup)
        MatchOperation matchGenre = Aggregation.match(Criteria.where("genreDetails.genre").is(genreName));

        // Step 3: Optional - project the necessary fields if needed
        AggregationOperation projectFields = Aggregation.project("pk", "isbn", "title", "description", "genre", "authors", "photo", "version");

        // Combine all steps in the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                lookupGenre,      // Lookup genres collection
                matchGenre,       // Match the genre name
                projectFields     // Optional: Project only necessary fields (optional)
        );

        // Execute the aggregation query
        AggregationResults<MongoBookModel> results = mt.aggregate(aggregation, "books", MongoBookModel.class);

        // Return the mapped list of books
        List<MongoBookModel> books = results.getMappedResults();

        // Map the results to your Book list
        return bookMapper.fromMongoBookModel(books);
    }


    @Override
    public List<Book> findByTitle(String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title)); // Accessing nested property
        List<MongoBookModel> list = mt.find(query, MongoBookModel.class);

        return bookMapper.fromMongoBookModel(list);
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("authors.name").is(authorName)); // Query on nested author collection
        List<MongoBookModel> list = mt.find(query, MongoBookModel.class);
        return bookMapper.fromMongoBookModel(list);
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        // Step 1: Match lendings with a start date greater than one year ago
        MatchOperation matchByDate = Aggregation.match(Criteria.where("startDate").gt(oneYearAgo));

        // Step 2: Group by book ID and count the number of lendings
        AggregationOperation groupByBookAndCount = Aggregation.group("bookId")
                .count().as("lendingCount");

        // Step 3: Sort by lending count in descending order
        SortOperation sortByLendingCountDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount"));

        // Step 4: Limit the results to the top 5 books
        AggregationOperation limit = Aggregation.limit(5);

        // Combine all operations into an aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                matchByDate,
                groupByBookAndCount,
                sortByLendingCountDesc,
                limit
        );

        // Execute the aggregation query
        AggregationResults<Document> results = mt.aggregate(aggregation, "lendings", Document.class);

        // Step 5: Retrieve the book details and map to BookCountDTO
        List<BookCountDTO> topBooks = results.getMappedResults().stream()
                .map(doc -> {
                    String bookId = doc.getString("pk");
                    long lendingCount = doc.getLong("lendingCount");

                    MongoBookModel mongoBook = mt.findById(bookId, MongoBookModel.class);
                    Book book = bookMapper.fromMongoBookModel(mongoBook);

                    return new BookCountDTO(book, lendingCount);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(topBooks, pageable, topBooks.size());
    }

    @Override
    public List<Book> findBooksByAuthorNumber(String authorNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("authors.authorNumber").is(authorNumber)); // Find books by author number
        List<MongoBookModel> list = mt.find(query, MongoBookModel.class);
        return bookMapper.fromMongoBookModel(list);
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        String title = query.getTitle();
        String genre = query.getGenre();
        String authorName = query.getAuthorName();

        Query mongoQuery = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            criteriaList.add(Criteria.where("title").regex("^" + title)); // Prefix match for title
        }

        if (genre != null && !genre.isEmpty()) {
            criteriaList.add(Criteria.where("genre").regex("^" + genre)); // Prefix match for genre
        }

        if (authorName != null && !authorName.isEmpty()) {
            criteriaList.add(Criteria.where("authors.name").regex("^" + authorName)); // Prefix match for author name
        }

        if (!criteriaList.isEmpty()) {
            mongoQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        //mongoQuery.with(page.toPageable()); // Apply pagination
        mongoQuery.with(Sort.by(Sort.Direction.ASC, "title")); // Sort by title alphabetically

        List<MongoBookModel> list = mt.find(mongoQuery, MongoBookModel.class);
        return bookMapper.fromMongoBookModel(list);
    }

    @Override
    public List<Book> findTopXBooksFromGenre(int x, String genre) {
        // Step 1: Lookup books collection to get book details for each lending entry
        AggregationOperation lookupBooks = Aggregation.lookup("books", "book.$id", "_id", "bookDetails");

        // Step 2: Unwind the bookDetails array to access individual book documents
        AggregationOperation unwindBookDetails = Aggregation.unwind("bookDetails");

        // Step 3: Lookup genres collection using the genre field from the bookDetails
        AggregationOperation lookupGenre = Aggregation.lookup("genres", "bookDetails.genre.$id", "_id", "genreDetails");

        // Step 4: Unwind the genreDetails array to access individual genre documents
        AggregationOperation unwindGenreDetails = Aggregation.unwind("genreDetails");

        // Step 5: Match only the documents where genreDetails.genre equals the specified genre name
        MatchOperation matchGenre = Aggregation.match(Criteria.where("genreDetails.genre").is(genre));

        // Step 6: Group by the book's _id and count the number of lendings
        AggregationOperation groupByBookAndCount = Aggregation.group("bookDetails._id")
                .count().as("lendingCount");

        // Step 7: Sort by lending count in descending order
        SortOperation sortByLendingCountDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount"));

        // Step 8: Limit the result to 'x' books
        AggregationOperation limit = Aggregation.limit(x);

        // Combine all operations into an aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                lookupBooks,
                unwindBookDetails,
                lookupGenre,
                unwindGenreDetails,
                matchGenre,
                groupByBookAndCount,
                sortByLendingCountDesc,
                limit
        );

        // Execute the aggregation query
        AggregationResults<Document> results = mt.aggregate(aggregation, "lendings", Document.class);
        List<Document> listAfterAggregation = results.getMappedResults();

        // Extract the book IDs from the aggregation results
        List<ObjectId> bookIds = listAfterAggregation.stream()
                .map(doc -> (ObjectId) doc.get("_id"))
                .collect(Collectors.toList());

        // Step 7: Fetch the actual books using the book IDs
        Criteria criteriaForBooks = Criteria.where("_id").in(bookIds);
        Query bookQuery = new Query(criteriaForBooks);

        List<MongoBookModel> books = mt.find(bookQuery, MongoBookModel.class, "books");

        return bookMapper.fromMongoBookModel(books);
    }


    @Override
    public Book save(Book book) {
        MongoBookModel mongoBook = bookMapper.toMongoBookModel(book);

        if (mongoBook.getPhoto() != null) {
            MongoPhotoModel photo = mongoBook.getPhoto();
            photo = mt.save(photo);
            mongoBook.setPhoto(photo);
        }

        MongoBookModel savedBook = mt.save(mongoBook);
        return bookMapper.fromMongoBookModel(savedBook);
    }

    @Override
    public void delete(Book book) {
        MongoBookModel mongoBook = bookMapper.toMongoBookModel(book);
        mt.remove(mongoBook);
    }

    @Override
    public List<Book> findAll() {
        List<MongoBookModel> mongoBooks = mt.findAll(MongoBookModel.class);
        return bookMapper.fromMongoBookModel(mongoBooks);
    }

    @Override
    public Optional<Book> findById(String bookId) {
        MongoBookModel mongoBook = mt.findById(bookId, MongoBookModel.class);

        return Optional.ofNullable(mongoBook)
                .map(bookMapper::fromMongoBookModel);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isbn").is(isbn));
        MongoBookModel mongoBook = mt.findOne(query, MongoBookModel.class);

        return Optional.ofNullable(mongoBook)
                .map(bookMapper::fromMongoBookModel);
    }
}
