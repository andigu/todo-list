package model.group;

import model.Identifiable;
import model.User;
import model.task.GroupTask;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andi Gu
 */
public class Group extends Identifiable {
    private Set<User> members;
    private Set<GroupTask> tasks;

    public Group(long id, String name) {
        super(id, name);
        members = new HashSet<>();
        tasks = new HashSet<>();
    }

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
