package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import pt.psoft.g1.psoftg1.bookmanagement.model.TopBook;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

public interface Top5BookRepository extends CRUDRepository<TopBook, String> {
    void deleteAll();
}
