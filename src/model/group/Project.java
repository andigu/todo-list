package model.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import model.Completable;
import model.User;
import model.task.ProjectTask;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A project holds a set of collaborators and project tasks. It is completable - there is a due date, and can be marked
 * as complete. Once it is marked as complete, it is shown as complete for all the collaborators within the project.
 *
 * @author Andi Gu
 */
public class Project extends Completable {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<User> collaborators;

    private Set<ProjectTask> tasks;

    public Project(String id, String name, Date dueDate) {
        super(id, name, dueDate);
        collaborators = new HashSet<>();
        tasks = new HashSet<>();
    }

    public void addCollaborator(User collaborator) {
        collaborators.add(collaborator);
    }

    public void removeCollaborator(User collaborator) {
        collaborators.remove(collaborator);
    }

    public Set<User> getCollaborators() {
        return collaborators;
    }

    public Set<ProjectTask> getTasks() {
        return tasks;
    }

    public void addTask(ProjectTask task) {
        tasks.add(task);
    }

    public void removeTask(ProjectTask task) {
        tasks.remove(task);
    }

    public Set<ProjectTask> getTasksAssignedTo(User user) {
        Set<ProjectTask> tasks = new HashSet<>();
        for (ProjectTask task : this.tasks) {
            if (task.assignedTo(user)) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    @Override
    public void complete(User user, Date completed) {

    }
}
