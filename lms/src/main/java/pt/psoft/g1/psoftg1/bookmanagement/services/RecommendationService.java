package pt.psoft.g1.psoftg1.bookmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;

public interface RecommendationService {
    List<Book> getRecommendationsForReader(String readerNumber);
}
