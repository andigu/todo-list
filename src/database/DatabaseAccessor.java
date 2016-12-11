package database;

import model.User;
import model.group.Group;
import model.group.Project;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;
import model.task.Task;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author Andi Gu
 */
public interface DatabaseAccessor {
    User getUserByLogin(String username, String password);

    User getUserById(String id);

    User getUserByToken(String token);

    Set<IndividualTask> getAllIndividualTasks(User user);

    Set<GroupTask> getAllGroupTasks(User user);

    Set<ProjectTask> getAllProjectTasks(User user);

    Set<Group> getGroups(User user);

    Set<Project> getProjects(User user);

    Set<GroupTask> getGroupTasks(Group group);

    Set<ProjectTask> getProjectTasks(Project project);

    Map<User, Date> getUsersCompletedGroupTask(GroupTask task);

    Group createGroup(String groupName) throws SQLIntegrityConstraintViolationException;

    void insertTask(Task task);

    void insertIndividualTask(IndividualTask individualTask);

    void insertGroupTask(GroupTask groupTask);

    void insertProjectTask(ProjectTask projectTask);

    void complete(IndividualTask task, Date dateCompleted);

    void complete(GroupTask task, Date dateCompleted);

    void complete(ProjectTask task, Date dateCompleted);

    void completeProject(Project project, Date dateCompleted);

    User registerUser(String username, String password, String firstName, String lastName) throws SQLIntegrityConstraintViolationException;

    String storeLogin(String userId);
}
