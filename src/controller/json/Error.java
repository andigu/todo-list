package controller.json;

/**
 * @author Andi Gu
 */
public class Error {
    private int code = 0;
    private String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
