package database;

import model.User;
import model.group.Group;
import model.group.Project;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.apache.derby.jdbc.ClientDataSource;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @author Andi Gu
 */
public final class DerbyDatabaseAccessor implements DatabaseAccessor {
    private static final String databaseName = "db";
    private static final ClientDataSource dataSource = new ClientConnectionPoolDataSource() {{
        setDatabaseName(databaseName);
    }};
    private static final DerbyDatabaseAccessor instance = new DerbyDatabaseAccessor();

    private static final String getUserByLoginSQL = "SELECT * FROM MODEL.USERS WHERE USERNAME = ? AND PASSWORD = ?";
    private static final String getUserByIdSQL = "SELECT * FROM MODEL.USERS WHERE USER_ID = ?";
    private static final String getUserByTokenSQL = "SELECT * FROM MODEL.USERS NATURAL JOIN APP.LOGINS WHERE TOKEN = ?";
    private static final String getAllIndividualTasksSQL = "SELECT * FROM MODEL.INDIVIDUAL_TASKS WHERE USER_ID = ?";
    private static final String getGroupsSQL = "SELECT * FROM MODEL.USER_GROUPS NATURAL JOIN MODEL.GROUPS WHERE USER_ID = ?";
    private static final String getAllGroupTasksSQL = "SELECT * FROM MODEL.GROUP_TASKS JOIN (SELECT * FROM MODEL.USER_GROUPS NATURAL JOIN MODEL.GROUPS WHERE USER_ID = ?) AS UGT ON MODEL.GROUP_TASKS.GROUP_ID = UGT.GROUP_ID";
    private static final String getProjectsSQL = "SELECT * FROM MODEL.USER_PROJECTS NATURAL JOIN MODEL.PROJECTS WHERE USER_ID = ?";
    private static final String getAllProjectTasksSQL = "SELECT * FROM MODEL.PROJECT_TASKS JOIN (SELECT * FROM MODEL.USER_PROJECTS NATURAL JOIN MODEL.PROJECTS WHERE USER_ID = ?) AS UPT ON MODEL.PROJECT_TASKS.PROJECT_ID = UPT.PROJECT_ID";
    private static final String getGroupTasksSQL = "SELECT * FROM MODEL.GROUP_TASKS WHERE GROUP_ID = ?";
    private static final String getProjectTasksSQL = "SELECT * FROM MODEL.PROJECT_TASKS WHERE PROJECT_ID = ?";
    private static final String getUsersCompletedGroupTaskSQL = "SELECT * FROM MODEL.USER_COMPLETED_GROUP_TASKS WHERE TASK_ID = ?";
    private static final String storeLoginSQL = "INSERT INTO APP.LOGINS(TOKEN, USER_ID) VALUES (?, ?)";
    private static final String registerUserSQL = "INSERT INTO MODEL.USERS(USER_ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME)  VALUES (?, ?, ?, ?, ?)";

    @Override
    public User getUserByLogin(String username, String password) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserByLoginSQL)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? ResultSetConverter.getUser(resultSet) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserById(String id) {
        return getUserByIdentification(id, "id");
    }

    @Override
    public User getUserByToken(String token) {
        return getUserByIdentification(token, "token");
    }

    /**
     * Functionality that is shared between getUserById and getUserByToken
     *
     * @param identification Unique identification value
     * @param idType Cannot directly pass in SQL: if this were possible, other functions might accidentally call this
     *               function and get unexpected results
     * @return User with matching identification
     */
    private User getUserByIdentification(String identification, String idType) {
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(idType.equals("token") ? getUserByTokenSQL : getUserByIdSQL)) {
            statement.setString(1, identification);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? ResultSetConverter.getUser(resultSet) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<IndividualTask> getAllIndividualTasks(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAllIndividualTasksSQL)) {
            statement.setString(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<IndividualTask> tasks = new HashSet<>();
            while (resultSet.next()) {
                tasks.add(ResultSetConverter.getIndividualTask(resultSet, user));
            }
            return tasks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<GroupTask> getAllGroupTasks(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAllGroupTasksSQL)) {
            statement.setString(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<GroupTask> groupTasks = new HashSet<>();
            Map<Long, Group> groupMap = new HashMap<>();
            long id;
            while (resultSet.next()) {
                id = resultSet.getLong("GROUP_ID");
                if (!groupMap.containsKey(id)) {
                    groupMap.put(id, ResultSetConverter.getGroup(resultSet));
                }
                groupTasks.add(ResultSetConverter.getGroupTask(resultSet, groupMap.get(id)));
            }
            return groupTasks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<ProjectTask> getAllProjectTasks(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAllProjectTasksSQL)) {
            statement.setString(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<ProjectTask> projectTasks = new HashSet<>();
            Map<String, Project> projectMap = new HashMap<>();
            String id;
            while (resultSet.next()) {
                id = resultSet.getString("PROJECT_ID");
                if (!projectMap.containsKey(id)) {
                    projectMap.put(id, ResultSetConverter.getProject(resultSet));
                }
                projectTasks.add(ResultSetConverter.getProjectTask(resultSet, projectMap.get(id)));
            }
            System.out.println("fdsfsdfds" + projectTasks);
            return projectTasks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Group> getGroups(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getGroupsSQL)) {
            statement.setString(1, user.getId());
            ResultSet result = statement.executeQuery();
            Set<Group> groups = new HashSet<>();
            while (result.next()) {
                groups.add(ResultSetConverter.getGroup(result));
            }
            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Project> getProjects(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getProjectsSQL)) {
            statement.setString(1, user.getId());
            ResultSet result = statement.executeQuery();
            Set<Project> projects = new HashSet<>();
            while (result.next()) {
                projects.add(ResultSetConverter.getProject(result));
            }
            return projects;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<GroupTask> getGroupTasks(Group group) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getGroupTasksSQL)) {
            statement.setString(1, group.getId());
            ResultSet result = statement.executeQuery();
            Set<GroupTask> tasks = new HashSet<>();
            while (result.next()) {
                tasks.add(ResultSetConverter.getGroupTask(result, group));
            }
            return tasks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<ProjectTask> getProjectTasks(Project project) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getProjectTasksSQL)) {
            statement.setString(1, project.getId());
            ResultSet result = statement.executeQuery();
            Set<ProjectTask> tasks = new HashSet<>();
            while (result.next()) {
                tasks.add(ResultSetConverter.getProjectTask(result, project));
            }
            return tasks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<User, Date> getUsersCompletedGroupTask(GroupTask task) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUsersCompletedGroupTaskSQL)) {
            statement.setString(1, task.getId());
            ResultSet result = statement.executeQuery();
            Map<User, Date> users = new HashMap<>();
            while (result.next()) {
                users.put(ResultSetConverter.getUser(result), result.getDate("DATE_COMPLETED"));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    @Override
    public String storeLogin(String userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(storeLoginSQL)) {
            String token = randomId();
            statement.setString(1, token);
            statement.setString(2, userId);
            statement.execute();
            return token;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param username  The username of the new user
     * @param password  The password of the new user
     * @param firstName The first name of the new user
     * @param lastName  The last name of the new user
     */
    @Override
    public void registerUser(String username, String password, String firstName, String lastName) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(registerUserSQL)) {
            statement.setString(1, randomId());
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, firstName);
            statement.setString(5, lastName);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String randomId() {
        return UUID.randomUUID().toString();
    }

    public static DerbyDatabaseAccessor getInstance() {
        return instance;
    }
}
