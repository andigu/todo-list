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
    private String icon; // Url for Facebook icon


    public Group(String id, String name) {
        super(id, name);
        members = new HashSet<>();
    }

    public Group(String name) {
        super(name);
    }

    public void setMembers(Set<User> members) {this.members = members;}

    public String getIcon() {
        return icon;
    }

    public void addMember(User member) {
        members.add(member);
    }

    public void removeMember(User member) {
        members.remove(member);
    }

    public Set<User> getMembers() {
        return members;
    }


}
