package controller.json;

/**
 * Holder class to send info info to frontend
 *
 * @author Andi Gu
 */
public class Status {
    private String name;
    private Object status;

    public Status(String name, Object status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public Object getStatus() {
        return status;
    }
}
