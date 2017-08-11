package model;

import model.facebook.FacebookEntity;

/**
 * A user holds a set of individual tasks, as well as a set of groups and projects that he or she belongs to.
 *
 * @author Andi Gu
 */

public class User extends FacebookEntity {
//    private String facebookId;
//    private String pictureUrl;
    private String email;
    private String firstName; // TODO deprecate
    private String lastName;

    public User(String id, String firstName, String lastName, String email, String facebookId, String pictureUrl) {
        this.setId(id);
        this.setName(firstName + " " + lastName);
        this.setFacebookId(facebookId);
        this.setPictureUrl(pictureUrl);
        this.firstName = firstName;
        this.lastName= lastName;
        this.email = email;
//        this.facebookId = facebookId;
//        this.pictureUrl = pictureUrl;
    }

    public User(String firstName, String lastName, String email, String facebookId, String pictureUrl) {
        super(firstName + " " + lastName);
        this.setFacebookId(facebookId);
        this.setPictureUrl(pictureUrl);
        this.firstName = firstName;
        this.lastName= lastName;
//        this.facebookId = facebookId;
//        this.pictureUrl = pictureUrl;
        this.email = email;

    }

    public User(String name) {
        super(name);
        this.setId(null);
    }

//    public String getFacebookId() {
//        return facebookId;
//    }
//
//    public String getPictureUrl() {
//        return pictureUrl;
//    }

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
                "facebookId='" + getFacebookId() + '\'' +
                ", pictureUrl='" + getPictureUrl() + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
