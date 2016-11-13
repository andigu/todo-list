package model;

import model.group.Group;
import model.group.Project;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;

import java.util.HashSet;
import java.util.Set;

/**
 * A user holds a set of individual tasks, as well as a set of groups and projects that he or she belongs to.
 *
 * @author Andi Gu
 */
public class User extends Identifiable {
    private Set<IndividualTask> individualTasks;
    private Set<Group> groups;
    private Set<Project> projects;
    private String fullName;

    public User(long id, String userName, String fullName) {
        super(id, userName);
        this.fullName = fullName;
        individualTasks = new HashSet<>();
        groups = new HashSet<>();
        projects = new HashSet<>();
    }

    public Set<IndividualTask> getIndividualTasks() {
        return individualTasks;
    }

    public void addIndividualTask(IndividualTask individualTask) {
        this.individualTasks.add(individualTask);
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void addProjects(Project project) {
        projects.add(project);
    }

    public Set<GroupTask> getGroupTasks() {
        Set<GroupTask> groupTasks = new HashSet<>();
        for (Group group : groups) {
            groupTasks.addAll(group.getTasks());
        }
        return groupTasks;
    }

    public Set<ProjectTask> getProjectTasks() {
        Set<ProjectTask> projectTasks = new HashSet<>();
        for (Project project : projects) {
            projectTasks.addAll(project.getTasksAssignedTo(this));
        }
        return projectTasks;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
