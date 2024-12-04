package pt.psoft.g1.psoftg1.recommendationmanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.FactoryBook;
import pt.psoft.g1.psoftg1.readermanagement.model.FactoryReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

@Setter
@Getter
public class Recommendation {

    private String pk;
    private Book book;
    private ReaderDetails readerDetails;
    private Long version;

    private FactoryBook _factoryBook;
    private FactoryReaderDetails _factoryReaderDetails;

    public Recommendation(FactoryBook _factoryBook, FactoryReaderDetails _factoryReaderDetails) {
        this._factoryBook = _factoryBook;
        this._factoryReaderDetails = _factoryReaderDetails;
    }

    public void defineBook(String isbn) {
        this.book = _factoryBook.newBook(isbn);
    }

    public void defineReaderDetails(String readerNumber) {
        this.readerDetails = _factoryReaderDetails.newReaderDetails(readerNumber);
    }
}
