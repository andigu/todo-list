package database;

import model.User;
import model.group.Group;
import model.group.Project;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author Andi Gu
 */
public interface DatabaseAccessor {
    User getUserByLogin(String username, String password) throws SQLException;

    User getUserById(long id) throws SQLException;

    Set<IndividualTask> getAllIndividualTasks(User user) throws SQLException;

    Set<GroupTask> getAllGroupTasks(User user) throws SQLException;

    Set<ProjectTask> getAllProjectTasks(User user) throws SQLException;

    Set<Group> getGroups(User user) throws SQLException;

    Set<Project> getProjects(User user) throws SQLException;

    Set<GroupTask> getGroupTasks(Group group) throws SQLException;

    Set<ProjectTask> getProjectTasks(Project project) throws SQLException;

    Map<User, Date> getUsersCompletedGroupTask(GroupTask task) throws SQLException;

    void complete(IndividualTask task, Date dateCompleted);

    void complete(GroupTask task, Date dateCompleted);

    void complete(ProjectTask task, Date dateCompleted);

    void completeProject(Project project, Date dateCompleted);
}
