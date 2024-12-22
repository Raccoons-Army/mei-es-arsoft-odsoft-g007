package pt.psoft.g1.psoftg1.bookmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.model.TopBook;

import java.util.List;

public interface Top5BookService {
    List<TopBook> findTop5BooksLent();
    void updateTop5BooksLent();
}
