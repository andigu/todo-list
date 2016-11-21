package database;

import model.User;
import model.group.Group;
import model.group.Project;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;
import org.apache.derby.jdbc.ClientDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Andi Gu
 */
public final class DerbyDatabaseAccessor implements DatabaseAccessor {
    private static final String databaseName = "db";
    private static final ClientDataSource dataSource = new ClientDataSource() {{
        setDatabaseName(databaseName);
    }};
    private static final DerbyDatabaseAccessor instance = new DerbyDatabaseAccessor();


    private static final String getUserByLoginSQL = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
    private static final String getUserByIdSQL = "SELECT * FROM USERS WHERE USER_ID = ?";
    private static final String getAllIndividualTasksSQL = "SELECT * FROM INDIVIDUAL_TASKS WHERE USER_ID = ?";
    private static final String getGroupsSQL = "SELECT USER_ID, GROUPS.GROUP_ID, GROUP_NAME FROM USER_GROUPS JOIN GROUPS ON USER_GROUPS.GROUP_ID = GROUPS.GROUP_ID WHERE USER_ID = ?";
    private static final String getAllGroupTasksSQL = "SELECT TASK_ID, GROUP_NAME, UGG.GROUP_ID, TASK_DUE_DATE FROM GROUP_TASKS JOIN (SELECT USER_ID, GROUPS.GROUP_ID, GROUP_NAME FROM USER_GROUPS JOIN GROUPS ON USER_GROUPS.GROUP_ID = GROUPS.GROUP_ID WHERE USER_ID = ?) " +
            "AS UGG ON GROUP_TASKS.GROUP_ID = UGG.GROUP_ID";


    public static DerbyDatabaseAccessor getInstance() {
        return instance;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("USER_ID"), resultSet.getString("USERNAME"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"));
    }

    private Group getGroupFromResultSet(ResultSet resultSet) throws SQLException {
        return new Group(resultSet.getLong("GROUP_ID"), resultSet.getString("GROUP_NAME"));
    }

    private GroupTask getGroupTaskFromResultSet(ResultSet resultSet, Group group) throws SQLException {
        return new GroupTask(resultSet.getLong("GROUP_TASK_ID"), resultSet.getString("GROUP_TASK_NAME"), resultSet.getDate("DUE_DATE"), group);
    }

    private IndividualTask getIndivididualTaskFromResultSet(ResultSet resultSet, User user) throws SQLException {
        return new IndividualTask(resultSet.getLong("INDIVIDUAL_TASK_ID"), resultSet.getString("NAME"), user, resultSet.getDate("DUE_DATE"));
    }

    @Override
    public User getUserByLogin(String username, String password) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserByLoginSQL)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? getUserFromResultSet(resultSet) : null;
        }
    }

    @Override
    public User getUserById(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserByIdSQL)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? getUserFromResultSet(resultSet) : null;
        }
    }

    @Override
    public Set<IndividualTask> getAllIndividualTasks(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAllIndividualTasksSQL)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<IndividualTask> tasks = new HashSet<>();
            while (resultSet.next()) {
                tasks.add(getIndivididualTaskFromResultSet(resultSet, user));
            }
            return tasks;
        }
    }

    @Override
    public Set<GroupTask> getAllGroupTasks(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAllGroupTasksSQL)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<GroupTask> groupTasks = new HashSet<>();
            Map<Long, Group> groupMap = new HashMap<>();
            long id;
            while (resultSet.next()) {
                id = resultSet.getLong("GROUP_ID");
                if (!groupMap.containsKey(id)) {
                    groupMap.put(id, getGroupFromResultSet(resultSet));
                }
                groupTasks.add(getGroupTaskFromResultSet(resultSet, groupMap.get(id)));
            }
            return groupTasks;
        }
    }

    @Override
    public Set<ProjectTask> getAllProjectTasks(User user) {
        return null;
    }

    @Override
    public Set<ProjectTask> getProjectTasksByDueDate(User user, Date dueDate) {
        return null;
    }

    @Override
    public Set<Group> getGroups(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getGroupsSQL)) {
            statement.setLong(1, user.getId());
            ResultSet result = statement.executeQuery();
            Set<Group> groups = new HashSet<>();
            while (result.next()) {
                groups.add(getGroupFromResultSet(result));
            }
            return groups;
        }
    }

    @Override
    public Set<Project> getProjects(User user) {
        return null;
    }

    @Override
    public Set<GroupTask> getGroupTasks(Group group) {
        return null;
    }

    @Override
    public Set<ProjectTask> getProjectTasks(Project project) {
        return null;
    }

    @Override
    public void complete(IndividualTask task, Date dateCompleted) {

    }

    @Override
    public void complete(GroupTask task, Date dateCompleted) {

    }

    @Override
    public void complete(ProjectTask task, Date dateCompleted) {

    }

    @Override
    public void completeProject(Project project, Date dateCompleted) {

    }
}
