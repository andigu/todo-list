package database;

import com.sun.prism.GraphicsPipeline;
import database.dao.GroupTaskDAO;
import database.dao.IndividualTaskDAO;
import database.dao.ProjectTaskDAO;
import database.dao.TaskDAO;
import model.User;
import model.group.Group;
import model.group.Project;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;
import model.task.Task;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.apache.derby.jdbc.ClientDataSource;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @author Andi Gu
 */
public final class DerbyDatabaseAccessor implements DatabaseAccessor { // TODO possible to template this?
    private static final String databaseName = "db";
    private static final ClientDataSource dataSource = new ClientConnectionPoolDataSource() {{
        setDatabaseName(databaseName);
    }};
    private static final DerbyDatabaseAccessor instance = new DerbyDatabaseAccessor();

    private static final String getUserByLoginSQL = "SELECT * FROM MODEL.USERS WHERE USERNAME = ? AND PASSWORD = ?";
    private static final String getUserByIdSQL = "SELECT * FROM MODEL.USERS WHERE USER_ID = ?";
    private static final String getUserByTokenSQL = "SELECT * FROM MODEL.USERS NATURAL JOIN APP.LOGINS WHERE TOKEN = ?";
    private static final String getGroupsSQL = "SELECT * FROM MODEL.USER_GROUPS NATURAL JOIN MODEL.GROUPS WHERE USER_ID = ?";
    private static final String getAllGroupsSQL = "SELECT * FROM MODEL.GROUPS";
    private static final String getProjectsSQL = "SELECT * FROM MODEL.USER_PROJECTS NATURAL JOIN MODEL.PROJECTS WHERE USER_ID = ?";
    private static final String getProjectTasksSQL = "SELECT * FROM MODEL.PROJECT_TASKS WHERE PROJECT_ID = ?";
    private static final String getUsersCompletedGroupTaskSQL = "SELECT * FROM MODEL.USER_COMPLETED_GROUP_TASKS WHERE TASK_ID = ?";
    private static final String storeLoginSQL = "INSERT INTO APP.LOGINS(TOKEN, USER_ID) VALUES (?, ?)";
    private static final String registerUserSQL = "INSERT INTO MODEL.USERS(USER_ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME)  VALUES (?, ?, ?, ?, ?)";
    private static final String createGroupSQL = "INSERT INTO MODEL.GROUPS (GROUP_ID, GROUP_NAME) VALUES (?, ?)";
    private static final String getGroupSQL = "SELECT * FROM MODEL.GROUPS WHERE GROUP_ID = ?";
    private static final String getProjectSQL = "SELECT * FROM MODEL.PROJECTS WHERE PROJECT_ID = ?";
    private static final String joinGroupSQL =   "INSERT INTO MODEL.USER_GROUPS (USER_ID, GROUP_ID) VALUES (?, ?)";

    private final Map<Class<? extends Task>, TaskDAO> taskDAOMap = new HashMap<Class<? extends Task>, TaskDAO>() {{
        put(GroupTask.class, new GroupTaskDAO(dataSource));
        put(IndividualTask.class, new IndividualTaskDAO(dataSource));
        put(ProjectTask.class, new ProjectTaskDAO(dataSource));
    }};

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

    @Override
    public Set<Task> getTasks(User user, Filter filter) {
        Set<Task> tasks = new HashSet<>();
        for (TaskDAO dao : taskDAOMap.values()) {
            tasks.addAll(dao.getAllTasks(user));
        }
        try {
            if (filter != null) {
                tasks = filter.doFilter(tasks);
            }
            return tasks;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Functionality that is shared between getUserById and getUserByToken
     *
     * @param identification Unique identification value
     * @param idType         Cannot directly pass in SQL: if this were possible, other functions might accidentally call this
     *                       function and get unexpected results
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
    public Set<Group> getAllGroups() {
        try(Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(getAllGroupsSQL);
            Set<Group> groupSet = new HashSet<>();
            while(resultSet.next()) {
                groupSet.add(ResultSetConverter.getGroup(resultSet));
            }
            return groupSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void joinGroup(String id, User user) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(joinGroupSQL)) {
            System.out.println("Reached db for joinGroup");
            statement.setString(1, user.getId());
            statement.setString(2, id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
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
    public User registerUser(String username, String password, String firstName, String lastName) throws SQLIntegrityConstraintViolationException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(registerUserSQL)) {
            String token = randomId();
            statement.setString(1, token);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, firstName);
            statement.setString(5, lastName);
            statement.execute();
            return new User(token, firstName, lastName, username);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String randomId() {
        return UUID.randomUUID().toString();
    }

    public static DerbyDatabaseAccessor getInstance() {
        return instance;
    }

    @Override
    public Group createGroup(String groupName) throws SQLIntegrityConstraintViolationException { // TODO change to create by Group
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(createGroupSQL)) {
            String id = randomId();
            statement.setString(1, id);
            statement.setString(2, groupName);
            return new Group(id, groupName);
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Group getGroupById(String id) {
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(getGroupSQL)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? ResultSetConverter.getGroup(resultSet) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Project getProjectById(String id) {
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(getProjectSQL)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? ResultSetConverter.getProject(resultSet) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insertTask(Task task) {
        taskDAOMap.get(task.getClass()).insertTask(task);
    }
}
