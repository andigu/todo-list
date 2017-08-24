package database;

import database.filter.Filter;
import database.filter.TaskFilter;
import model.Session;
import model.User;
import model.group.Group;
import model.group.Project;
import model.group.Topic;
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
    //User getUserByLogin(String username, String password);

    User getUserByFacebookId(String id);

    User getUserById(String id);

    User getUserByToken(String token);

    Set<Task> getTasks(User user, TaskFilter taskFilter);

    Set<Group> getGroups(User user, Filter<Group> filter);

    Set<GroupTask> getGroupTasks(Group group);

    Set<User> getMembersOf(Group group);

    Map<User, Date> getUsersCompletedGroupTask(GroupTask task);

    Group createGroup(Group group) throws SQLIntegrityConstraintViolationException;

    Group getGroupByFacebookId(String facebookId);

    void joinGroup (String id, User user) throws SQLException;

    Group getGroupById(String id);

    void insertTask(Task task);

    void complete(IndividualTask task, Date dateCompleted);

    void complete(GroupTask task, Date dateCompleted);

    Topic insertTopic(Topic topic);

    User registerUser(String firstName, String lastName, String email, String facebookId, String pictureUrl) throws SQLIntegrityConstraintViolationException;

    User registerUser(User user) throws SQLIntegrityConstraintViolationException;

    User updateUser(User user);

    Session storeLogin(String userId, String facebookToken);
    
    String getFacebookTokenBySession(Session session);

    void deleteLogin(String userId);

    Set<Topic> getTopics(Filter<Topic> filter);

}
