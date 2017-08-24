package model.group;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import model.Identifiable;
import model.User;
import model.facebook.FacebookEntity;
import model.task.GroupTask;

import java.util.HashSet;
import java.util.Set;

/**
 * A group holds a set of users and tasks. It is ongoing and non-completable, analogous to a class at school.
 *
 * @author Andi Gu
 */
public class Group extends FacebookEntity {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<User> members;

    private Set<Topic> topics;

    public Group(String id, String name) {
        super(id, name);
        members = new HashSet<>();
    }

    public Group(String name) {
        super(name);
    }

    // From facebook
    public Group(String facebookId, String name, String privacy, String pictureUrl) {
        this.setId(null);
        this.setFacebookId(facebookId);
        this.setName(name);
        this.setPictureUrl(pictureUrl);
    }

    // From db
    public Group(String id, String name, String facebookId, String pictureUrl, String privacy) {
        this.setId(id);
        this.setName(name);
        this.setFacebookId(facebookId);
        this.setPictureUrl(pictureUrl);
    }


    public void setMembers(Set<User> members) {this.members = members;}


    public void addMember(User member) {
        members.add(member);
    }

    public void removeMember(User member) {
        members.remove(member);
    }

    public Set<User> getMembers() {
        return members;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }
}
