package model.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import model.Completable;
import model.User;
import model.group.Project;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * A project task can be assigned to a subset of the collaborators of the project it belongs to. For example, if
 * A, B, C, and D are working on a project, that project may hold a task that is assigned to A, B, and D. Now, users
 * A, B, and D have full access to that task. User C does not have full access to the task - he/she may <b>not</b>
 * mark it as complete. On the other hand, a project may <b>not</b> contain a task that is assigned to A, B, and E,
 * because E is not a collaborator in the project.
 * </p>
 * <p>
 * Once a task is marked as complete by one of its assignees, it is complete for everyone in the project it belongs to.
 * Completion is shared across the project.
 * </p>
 *
 * @author Andi Gu
 */
public class ProjectTask extends Task {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<User> assignees;
    private Project project;
    private boolean complete;

    public ProjectTask(String id, String name, Date dueDate, Project project) {
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
