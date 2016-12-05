package controller.json;

/**
 * Holder for types of errors the backend might encounter
 *
 * @author Andi Gu
 */
public enum ErrorType {
    DuplicateKey;

    public String toString() {
        return name();
    }
}
