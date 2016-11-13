package model.task;

import model.Completable;
import model.User;
import model.group.Project;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andi Gu
 */
public class ProjectTask extends Completable {
    private Set<User> assignees;
    private Project project;
    private boolean complete;

    public ProjectTask(long id, String name, Date dueDate, Project project) {
        super(id, name, dueDate);
        this.project = project;
        complete = false;
        assignees = new HashSet<>();
    }

    public void addAssignee(User user) {
        assignees.add(user);
    }

    public boolean assignedTo(User user) {
        return assignees.contains(user);
    }

    @Override
    public void complete(User user, Date completed) {
        complete = true;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isComplete() {
        return complete;
    }
}
