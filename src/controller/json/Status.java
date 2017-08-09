package controller.json;

/**
 * Holder class to send info info to frontend
 *
 * @author Andi Gu
 */
public class Status {
    private String name;
    private Object state;

    public Status(String name, Object state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public Object getState() {
        return state;
    }
}
