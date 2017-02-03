package model;

/**
 * A user holds a set of individual tasks, as well as a set of groups and projects that he or she belongs to.
 *
 * @author Andi Gu
 */

public class User extends Identifiable {
    private String username;
    private String email;

    public User(String id, String firstName, String lastName, String username, String email) {
        super(id, firstName + " " + lastName);
        this.username = username;
        this.email = email;
    }

    public User(String firstName, String lastName, String username) {
        super(firstName + " " + lastName);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
