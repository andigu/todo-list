package model;

/**
 * A user holds a set of individual tasks, as well as a set of groups and projects that he or she belongs to.
 *
 * @author Andi Gu
 */
public class User extends Identifiable {
    private String firstName;
    private String lastName;

    public User(long id, String userName, String fullName) {
        super(id, userName);
        String[] splitName = fullName.split(" ");
        firstName = splitName[0];
        lastName = splitName[1];
    }

    public User(long id, String userName, String firstName, String lastName) {
        super(id, userName);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        String[] splitName = fullName.split(" ");
        firstName = splitName[0];
        lastName = splitName[1];
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
