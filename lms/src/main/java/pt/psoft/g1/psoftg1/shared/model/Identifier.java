package pt.psoft.g1.psoftg1.shared.model;

public class Identifier<T> {
    private T value;

    public Identifier(T value) {
        this.value = value;
    }

    public boolean equals(Identifier<T> id) {
        if (id == null || id.toValue() == null) {
            return false;
        }
        if (!id.getClass().equals(this.getClass())) {
            return false;
        }
        return id.toValue().equals(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Return raw value of identifier
     */
    public T toValue() {
        return value;
    }
}
