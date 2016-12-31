package model.group;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import model.Identifiable;
import model.User;
import model.task.GroupTask;

import java.util.HashSet;
import java.util.Set;

/**
 * A group holds a set of users and tasks. It is ongoing and non-completable, analogous to a class at school.
 *
 * @author Andi Gu
 */
public class Group extends Identifiable {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<User> members;

    @JsonManagedReference
    private Set<GroupTask> tasks;

    public Group(String id, String name) {
        super(id, name);
        members = new HashSet<>();
        tasks = new HashSet<>();
    }

    public void setMembers(Set<User> members) {this.members = members;}

    public void setTasks (Set<GroupTask> tasks) {this.tasks = tasks;}

    public void addMember(User member) {
        members.add(member);
    }

    public void removeMember(User member) {
        members.remove(member);
    }

    public Set<GroupTask> getTasks() {
        return tasks;
    }

    public void addTask(GroupTask task) {
        tasks.add(task);
    }

    public void removeTask(GroupTask task) {
        tasks.remove(task);
    }

    public Set<User> getMembers() {
        return members;
    }


}
