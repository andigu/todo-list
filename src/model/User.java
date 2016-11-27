package model;

/**
 * A user holds a set of individual tasks, as well as a set of groups and projects that he or she belongs to.
 *
 * @author Andi Gu
 */

public class User extends Identifiable {
    private String username;

    public User(long id, String name, String username) {
        super(id, name);
        this.username = username;
    }

    public User(long id, String firstName, String lastName, String username) {
        super(id, firstName + " " + lastName);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
