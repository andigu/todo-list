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
    private static final String getAllGroupTasksSQL = "SELECT TASK_ID, GROUP_NAME, UGT.GROUP_ID, TASK_DUE_DATE FROM GROUP_TASKS JOIN (SELECT USER_ID, GROUPS.GROUP_ID, GROUP_NAME FROM USER_GROUPS JOIN GROUPS ON USER_GROUPS.GROUP_ID = GROUPS.GROUP_ID WHERE USER_ID = ?) " +
            "AS UGT ON GROUP_TASKS.GROUP_ID = UGT.GROUP_ID";
    private static final String getProjectsSQL = "SELECT USER_ID, PROJECTS.PROJECT_ID, PROJECT_NAME, PROJECT_DUE_DATE FROM USER_PROJECTS JOIN PROJECTS ON USER_PROJECTS.PROJECT_ID = PROJECTS.PROJECT_ID WHERE USER_ID = ?";
    private static final String getAllProjectTasksSQL = "SELECT TASK_ID, PROJECT_NAME, UPT.PROJECT_ID, TASK_DUE_DATE FROM PROJECT_TASKS JOIN (SELECT USER_ID, PROJECTS.PROJECT_ID, PROJECT_NAME, PROJECT_DUE_DATE FROM USER_PROJECTS JOIN PROJECTS ON USER_PROJECTS.PROJECT_ID = PROJECTS.PROJECT_ID WHERE USER_ID = ?) " +
            "AS UPT ON PROJECT_TASKS.PROJECT_ID = UPT.PROJECT_ID";


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
