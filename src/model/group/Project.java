package model.group;

import model.Completable;
import model.User;
import model.task.ProjectTask;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andi Gu
 */
public class Project extends Completable {
    private Set<User> collaborators;
    private Set<ProjectTask> tasks;

    public Project(long id, String name, Date dueDate) {
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
