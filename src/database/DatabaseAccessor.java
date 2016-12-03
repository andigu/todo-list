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
    User getUserByLogin(String username, String password);

    User getUserById(long id);

    Set<IndividualTask> getAllIndividualTasks(User user);

    Set<GroupTask> getAllGroupTasks(User user);

    Set<ProjectTask> getAllProjectTasks(User user);

    Set<Group> getGroups(User user);

    Set<Project> getProjects(User user);

    Set<GroupTask> getGroupTasks(Group group);

    Set<ProjectTask> getProjectTasks(Project project);

    Map<User, Date> getUsersCompletedGroupTask(GroupTask task);

    void complete(IndividualTask task, Date dateCompleted);

    void complete(GroupTask task, Date dateCompleted);

    void complete(ProjectTask task, Date dateCompleted);

    void completeProject(Project project, Date dateCompleted);

    void registerUser(String username, String password, String firstName, String lastName);

    Long storeLogin(Long userId);
}
