package model;

/**
 * A user holds a set of individual tasks, as well as a set of groups and projects that he or she belongs to.
 *
 * @author Andi Gu
 */

public class User extends Identifiable {
    private String facebookId;
    private String pictureUrl;
    private String email;
    private String firstName;
    private String lastName;

    public User(String id, String firstName, String lastName, String email, String facebookId, String pictureUrl) {
        super(id, firstName + " " + lastName);
        this.firstName = firstName;
        this.lastName= lastName;
        this.email = email;
        this.facebookId = facebookId;
        this.pictureUrl = pictureUrl;
    }

    public User(String firstName, String lastName, String email, String facebookId, String pictureUrl) {
        super(firstName + " " + lastName);
        this.firstName = firstName;
        this.lastName= lastName;
        this.facebookId = facebookId;
        this.pictureUrl = pictureUrl;
        this.email = email;

    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "facebookId='" + facebookId + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
