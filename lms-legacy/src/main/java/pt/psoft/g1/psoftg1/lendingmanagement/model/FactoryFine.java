package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.springframework.stereotype.Component;

@Component
public class FactoryFine {

    private FactoryLending _factoryLending;

    public Fine newFine() {
        return new Fine(_factoryLending);
    }
}
