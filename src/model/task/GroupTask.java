package model.task;

import model.Completable;
import model.User;
import model.group.Group;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * A group task applies to all users in the group it belongs to. For example, if A, B, C, and D are in a group,
 * and a certain task is added to that group, then each of the users A, B, C, and D now have full access to that task.
 * </p>
 * <p>
 * When a group task is marked as complete by a user, it is <b>not</b> marked as complete for other users within the
 * group (completion is <b>not</b> shared across the group). However, each group task holds a set of users who have
 * completed the task.
 * </p>
 *
 * @author Andi Gu
 */

public class GroupTask extends Task {
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

    @Override
    public void complete(User user, Date completed) {
        this.completed.add(user);
    }

    public void complete(Map<User, Date> completedUsers) {
        for (Map.Entry<User, Date> entry : completedUsers.entrySet()) {
            complete(entry.getKey(), entry.getValue());
        }
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
