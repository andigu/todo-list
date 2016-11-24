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
    private static final String getGroupsSQL = "SELECT * FROM USER_GROUPS NATURAL JOIN GROUPS WHERE USER_ID = ?";
    private static final String getAllGroupTasksSQL = "SELECT * FROM GROUP_TASKS NATURAL JOIN (SELECT * FROM USER_GROUPS NATURAL JOIN GROUPS WHERE USER_ID = ?) AS UGT";
    private static final String getProjectsSQL = "SELECT * FROM USER_PROJECTS NATURAL JOIN PROJECTS WHERE USER_ID = ?";
    private static final String getAllProjectTasksSQL = "SELECT * FROM PROJECT_TASKS NATURAL JOIN (SELECT * FROM USER_PROJECTS NATURAL JOIN PROJECTS WHERE USER_ID = ?) AS UPT";
    private static final String getGroupTasksSQL = "SELECT * FROM GROUP_TASKS WHERE GROUP_ID = ?";
    private static final String getProjectTasksSQL = "SELECT * FROM PROJECT_TASKS WHERE PROJECT_ID = ?";
    private static final String getUsersCompletedGroupTaskSQL = "SELECT * FROM USER_COMPLETED_GROUP_TASKS WHERE TASK_ID = ?";


    public static DerbyDatabaseAccessor getInstance() {
        return instance;
    }

    @Override
    public User getUserByLogin(String username, String password) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserByLoginSQL)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? ObjectMapper.getUser(resultSet) : null;
        }
    }

    @Override
    public User getUserById(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserByIdSQL)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? ObjectMapper.getUser(resultSet) : null;
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
                tasks.add(ObjectMapper.getIndividualTask(resultSet, user));
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
                    groupMap.put(id, ObjectMapper.getGroup(resultSet));
                }
                groupTasks.add(ObjectMapper.getGroupTask(resultSet, groupMap.get(id)));
            }
            return groupTasks;
        }
    }

    @Override
    public Set<ProjectTask> getAllProjectTasks(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAllProjectTasksSQL)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<ProjectTask> projectTasks = new HashSet<>();
            Map<Long, Project> projectMap = new HashMap<>();
            long id;
            while (resultSet.next()) {
                id = resultSet.getLong("PROJECT_ID");
                if (!projectMap.containsKey(id)) {
                    projectMap.put(id, ObjectMapper.getProject(resultSet));
                }
                projectTasks.add(ObjectMapper.getProjectTask(resultSet, projectMap.get(id)));
            }
            return projectTasks;
        }
    }

    @Override
    public Set<Group> getGroups(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getGroupsSQL)) {
            statement.setLong(1, user.getId());
            ResultSet result = statement.executeQuery();
            Set<Group> groups = new HashSet<>();
            while (result.next()) {
                groups.add(ObjectMapper.getGroup(result));
            }
            return groups;
        }
    }

    @Override
    public Set<Project> getProjects(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getProjectsSQL)) {
            statement.setLong(1, user.getId());
            ResultSet result = statement.executeQuery();
            Set<Project> projects = new HashSet<>();
            while (result.next()) {
                projects.add(ObjectMapper.getProject(result));
            }
            return projects;
        }
    }

    @Override
    public Set<GroupTask> getGroupTasks(Group group) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getGroupTasksSQL)) {
            statement.setLong(1, group.getId());
            ResultSet result = statement.executeQuery();
            Set<GroupTask> tasks = new HashSet<>();
            while (result.next()) {
                tasks.add(ObjectMapper.getGroupTask(result, group));
            }
            return tasks;
        }
    }

    @Override
    public Set<ProjectTask> getProjectTasks(Project project) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getProjectTasksSQL)) {
            statement.setLong(1, project.getId());
            ResultSet result = statement.executeQuery();
            Set<ProjectTask> tasks = new HashSet<>();
            while (result.next()) {
                tasks.add(ObjectMapper.getProjectTask(result, project));
            }
            return tasks;
        }
    }

    @Override
    public Map<User, Date> getUsersCompletedGroupTask(GroupTask task) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUsersCompletedGroupTaskSQL)) {
            statement.setLong(1, task.getId());
            ResultSet result = statement.executeQuery();
            Map<User, Date> users = new HashMap<>();
            while (result.next()) {
                users.put(ObjectMapper.getUser(result), result.getDate("DATE_COMPLETED"));
            }
            return users;
        }
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
