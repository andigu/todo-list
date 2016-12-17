package controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import controller.json.Error;
import controller.json.Status;

/**
 * @author Andi Gu
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntity<T> {
    private Error error;
    private Status status;
    private T data;

    ResponseEntity() {
        this.error = null;
        this.status = null;
        this.data = null;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
