package pt.psoft.g1.psoftg1.authormanagement.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopAuthor {

    private String name;
    private long lendingCount;

    public TopAuthor(String name, long lendingCount) {
        this.name = name;
        this.lendingCount = lendingCount;
    }
}

