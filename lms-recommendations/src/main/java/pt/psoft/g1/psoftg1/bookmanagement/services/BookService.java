package pt.psoft.g1.psoftg1.bookmanagement.services;


import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;

/**
 *
 */
public interface BookService {
    void create(BookViewAMQP request);
}
