package controller.json;

/**
 * Class to transmit error information from backend to frontend
 *
 * @author Andi Gu
 */
public class Error {
    private String error;

    public Error(ErrorType errorType) {
        error = errorType.toString();
    }

    public String getError() {
        return error;
    }
}
