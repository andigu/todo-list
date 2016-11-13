package model.task;

import model.Completable;
import model.User;
import model.group.Group;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andi Gu
 */

public class GroupTask extends Completable {
    private Group group;
    private Set<User> completed;

    public GroupTask(long id, String name, Date dueDate, Group group) {
        super(id, name, dueDate);
        this.group = group;
        completed = new HashSet<>();
    }

    public Set<User> getCompleted() {
        return completed;
    }

    public Set<User> getUncompleted() {
        Set<User> allUsers = new HashSet<>(group.getMembers()); // Copy of the set of all users of the class
        allUsers.removeAll(completed);
        return allUsers;
    }

    @Override
    public void complete(User user, Date completed) {

    }
}
