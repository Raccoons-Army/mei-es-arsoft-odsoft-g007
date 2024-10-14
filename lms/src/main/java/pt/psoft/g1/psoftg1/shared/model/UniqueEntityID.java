package pt.psoft.g1.psoftg1.shared.model;

import pt.psoft.g1.psoftg1.shared.model.Identifier;

import java.util.UUID;

public class UniqueEntityID extends Identifier<String> {

    public UniqueEntityID() {
        super(UUID.randomUUID().toString());
    }

    public UniqueEntityID(String id) {
        super(id != null ? id : UUID.randomUUID().toString());
    }

    public UniqueEntityID(Long id) {
        super(id != null ? id.toString() : UUID.randomUUID().toString());
    }
}
