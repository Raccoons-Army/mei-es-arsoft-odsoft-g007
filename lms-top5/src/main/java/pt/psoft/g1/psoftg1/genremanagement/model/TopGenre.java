package pt.psoft.g1.psoftg1.genremanagement.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopGenre {
    String genre;

    long lendingCount;

    protected TopGenre() {
    }

    public TopGenre(String genre, long lendingCount) {
        this.genre = genre;
        this.lendingCount = lendingCount;
    }

    public String toString() {
        return genre;
    }
}
