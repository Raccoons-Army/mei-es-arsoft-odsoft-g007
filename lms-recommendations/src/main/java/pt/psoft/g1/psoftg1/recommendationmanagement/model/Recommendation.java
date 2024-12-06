package pt.psoft.g1.psoftg1.recommendationmanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.FactoryBook;
import pt.psoft.g1.psoftg1.readermanagement.model.FactoryReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;

@Setter
@Getter
public class Recommendation {

    private String pk;
    private Book book;
    private ReaderDetails readerDetails;
    private boolean positive;
    private Long version;
    private LocalDate createdAt;

    private FactoryBook _factoryBook;
    private FactoryReaderDetails _factoryReaderDetails;

    public Recommendation(FactoryBook _factoryBook, FactoryReaderDetails _factoryReaderDetails, boolean isPositive) {
        this._factoryBook = _factoryBook;
        this._factoryReaderDetails = _factoryReaderDetails;
        this.positive = isPositive;
        this.createdAt = LocalDate.now();
    }

    public void defineBook(String isbn) {
        this.book = _factoryBook.newBook(isbn);
    }

    public void defineBook(Book book) {
        this.book = book;
    }

    public void defineReaderDetails(String readerNumber) {
        this.readerDetails = _factoryReaderDetails.newReaderDetails(readerNumber);
    }

    public void defineReaderDetails(ReaderDetails readerDetails) {
        this.readerDetails = readerDetails;
    }
}
