package database;

import model.User;
import model.group.Group;
import model.group.Project;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;

import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

/**
 * @author Andi Gu
 */
public interface DatabaseAccessor {
    public User getUserByLogin(String username, String password) throws SQLException;

    public User getUserById(long id) throws SQLException;

    public Set<IndividualTask> getAllIndividualTasks(User user) throws SQLException;

    public Set<GroupTask> getAllGroupTasks(User user) throws SQLException;

    public Set<ProjectTask> getAllProjectTasks(User user);

    public Set<ProjectTask> getProjectTasksByDueDate(User user, Date dueDate);

    public Set<Group> getGroups(User user) throws SQLException;

    public Set<Project> getProjects(User user);

    public Set<GroupTask> getGroupTasks(Group group);

    public Set<ProjectTask> getProjectTasks(Project project);

    public void complete(IndividualTask task, Date dateCompleted);

    public void complete(GroupTask task, Date dateCompleted);

    public void complete(ProjectTask task, Date dateCompleted);

    public void completeProject(Project project, Date dateCompleted);
}
