package pt.psoft.g1.psoftg1.bookmanagement.model;

import pt.psoft.g1.psoftg1.shared.model.UniqueEntityID;

public class BookId extends UniqueEntityID {
    public BookId() {
        super(); // Calls UniqueEntityID's constructor which generates a UUID
    }

    public BookId(String id) {
        super(id); // Passes an existing ID to the parent constructor
    }

    public BookId(Long id) {
        super(id); // Passes an existing numeric ID to the parent constructor
    }
}
