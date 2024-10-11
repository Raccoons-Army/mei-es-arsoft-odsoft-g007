package pt.psoft.g1.psoftg1.bookmanagement.model;

public class Title {
    String title;

    protected Title() {}

    public Title(String title) {
        setTitle(title);
    }

    public void setTitle(String title) {

/*
        if (!StringUtilsCustom.startsOrEndsInWhiteSpace(title)) {
            throw new IllegalArgumentException("Invalid title: " + title);
        }
*/
        if(title == null)
            throw new IllegalArgumentException("Title cannot be null");
        if(title.isBlank())
            throw new IllegalArgumentException("Title cannot be blank");
        int TITLE_MAX_LENGTH = 128;
        if(title.length() > TITLE_MAX_LENGTH)
            throw new IllegalArgumentException("Title has a maximum of " + TITLE_MAX_LENGTH + " characters");
        this.title = title.strip();
    }

    public String toString() {
        return this.title;
    }
}
