package pt.psoft.g1.psoftg1.bookmanagement.services.recommendationStrategies;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.List;

public interface RecommendationAgeBasedStrategy {
    List<Book> recommend(ReaderDetails readerDetails);
}

