package database;

import database.dao.GroupTaskDAO;
import database.dao.IndividualTaskDAO;
import database.dao.ProjectTaskDAO;
import database.dao.TaskDAO;
import database.filter.Filter;
import database.filter.TaskFilter;
import encryption.Encryption;
import model.Session;
import model.User;
import model.group.Group;
import model.group.Project;
import model.group.Topic;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;
import model.task.Task;

import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Date;
import java.util.*;



/**
 * @author Andi Gu, Susheel Kona
 */
public final class SQLDatabaseAccessor implements DatabaseAccessor {
    private static final String SOURCE_FLAVOR = "postgres_local";
    private static final Map<String, DataSource> sources = new HashMap<String, DataSource>() {{
        put("derby_local", new ClientConnectionPoolDataSource(){{
            setDatabaseName("db");
        }});

        put("postgres_local", new Jdbc3PoolingDataSource(){{
            //Todo read all this from a file
            setServerName("localhost");
            setDatabaseName("todo-list");
            setUser("postgres"); //running as root user like Jeff Dean
            setPassword("root");

        }});

        //Heroku Native
        //*Do not use while running locally, change to this before deploying to heroku*
        put("postgres_heroku", new Jdbc3PoolingDataSource(){{
                try{
                    //Does not exist on non-heroku vms, exception will be thrown
                    URI dbUri = new URI(System.getenv("DATABASE_URL"));
                    System.out.println(System.getenv("DATABASE_URL"));
                    String username = dbUri.getUserInfo().split(":")[0];
                    String password  = dbUri.getUserInfo().split(":")[1];
                    setServerName(dbUri.getHost());
                    setDatabaseName(dbUri.getPath().replaceFirst("/", ""));
                    setUser(username);
                    setPassword(password);
                    setMaxConnections(10);

                } catch (Exception e){
                    e.printStackTrace();
                }
        }});
        
    }};

    //Todo make sure these work with postgres
    //private static final String getUserByLoginSQL = "SELECT * FROM MODEL.USERS WHERE USERNAME = ? AND PASSWORD = ?";
    private static final String getUserByIdSQL = "SELECT * FROM MODEL.USERS WHERE USER_ID = ?";
    private static final String getUserByFacebookIdSQL = "SELECT * FROM MODEL.USERS WHERE FACEBOOK_ID = ?";
    private static final String getFacebookTokenByLoginToken = "SELECT FACEBOOK_TOKEN FROM APP.LOGINS WHERE TOKEN = ?";

    private static final String getMembersOfGroup = "SELECT * FROM MODEL.USERS NATURAL JOIN MODEL.USER_GROUPS WHERE GROUP_ID = ?";
    private static final String getUserByTokenSQL = "SELECT * FROM MODEL.USERS NATURAL JOIN APP.LOGINS WHERE TOKEN = ?";
    //private static final String getGroupsSQL = "SELECT * FROM MODEL.USER_GROUPS NATURAL JOIN MODEL.GROUPS WHERE USER_ID = ?";
    private static final String getGroupsSQL = "SELECT * FROM MODEL.GROUPS";
    private static final String getProjectsSQL = "SELECT * FROM MODEL.USER_PROJECTS NATURAL JOIN MODEL.PROJECTS WHERE USER_ID = ?";
    private static final String getProjectTasksSQL = "SELECT * FROM MODEL.PROJECT_TASKS WHERE PROJECT_ID = ?";
    private static final String getUsersCompletedGroupTaskSQL = "SELECT * FROM MODEL.USER_COMPLETED_GROUP_TASKS WHERE TASK_ID = ?";
    private static final String storeLoginSQL = "INSERT INTO APP.LOGINS(TOKEN, USER_ID, FACEBOOK_TOKEN) VALUES (?, ?, ?)";
    private static final String deleteLoginSQL = "DELETE  FROM APP.LOGINS WHERE user_id = ?";
    private static final String registerUserSQL = "INSERT INTO MODEL.USERS(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, FACEBOOK_ID, PICTURE_URL)  VALUES (?, ?, ?, ?, ?, ?)";
    private static final String updateUserSQL = "UPDATE MODEL.USERS SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, PICTURE_URL = ? WHERE USER_ID = ?";
    private static final String createGroupSQL = "INSERT INTO MODEL.GROUPS (GROUP_ID, GROUP_NAME, FACEBOOK_ID, PICTURE_URL) VALUES (?, ?, ?, ?)";
    private static final String getGroupSQL = "SELECT * FROM MODEL.GROUPS WHERE GROUP_ID = ?";
    private static final String getGroupByFacebookIdSQL = "SELECT * FROM MODEL.GROUPS WHERE FACEBOOK_ID = ?";
    private static final String joinGroupSQL = "INSERT INTO MODEL.USER_GROUPS (USER_ID, GROUP_ID) VALUES (?, ?)";

    private static final String getTopicsByGroupSQL = "SELECT * FROM MODEL.TOPICS WHERE group_id = ?";
    private static final String getTopicsSQL = "SELECT * FROM MODEL.TOPICS";
    private static final String insertTopicSQL = "INSERT INTO MODEL.TOPICS (TOPIC_ID, TOPIC_NAME, GROUP_ID, START_DATE) VALUES(?, ?, ?, ?)";


    private final Map<Class<? extends Task>, TaskDAO> taskDAOMap = new HashMap<Class<? extends Task>, TaskDAO>() {{
        put(GroupTask.class, new GroupTaskDAO(sources.get(SOURCE_FLAVOR)));
        put(IndividualTask.class, new IndividualTaskDAO(sources.get(SOURCE_FLAVOR)));
        put(ProjectTask.class, new ProjectTaskDAO(sources.get(SOURCE_FLAVOR)));
    }};

    private static final SQLDatabaseAccessor instance = new SQLDatabaseAccessor();


    @Override
    public User getUserByFacebookId(String id) {
        return getUserByIdentification(id, "facebook_id");
    }

    @Override
    public User getUserById(String id) {
        return getUserByIdentification(id, "id");
    }

    /**
     * Functionality that is shared between getUserById, getUserByToken and getUserByFacebookId
     *
     * @param identification Unique identification value
     * @param idType         Cannot directly pass in SQL: if this were possible, other functions might accidentally call this
     *                       function and get unexpected results
     * @return User with matching identification
     */
    private User getUserByIdentification(String identification, String idType) {
        Map<String, String> idTypeMap = new HashMap<String, String>() {{
            put("token", getUserByTokenSQL);
            put("id", getUserByIdSQL);
            put("facebook_id", getUserByFacebookIdSQL);
        }};
        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = connection.prepareStatement(idTypeMap.get(idType))) {
            statement.setString(1, identification);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? ResultSetConverter.getUser(resultSet) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFacebookTokenBySession(Session session) {
        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = connection.prepareStatement(getFacebookTokenByLoginToken)) {
            statement.setString(1, session.getLoginToken());
            System.out.println(session);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? decrypt(resultSet.getString("facebook_token"), session.getFacebookTokenKey()) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByToken(String token) {
        return getUserByIdentification(token, "token");
    }


    @Override
    public Set<Task> getTasks(User user, TaskFilter taskFilter) {
        Set<Task> tasks = new HashSet<>();
        for (TaskDAO dao : taskDAOMap.values()) {
            tasks.addAll(dao.getAllTasks(user));
        }
        try {
            if (taskFilter != null) {
                tasks = taskFilter.doFilter(tasks);
            }
            return tasks;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Set<Group> getGroups(User user, Filter<Group> filter) {
        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = connection.prepareStatement(getGroupsSQL)) {
            ResultSet result = statement.executeQuery();
            Set<Group> groups = new HashSet<>();
            while (result.next()) {
                Group group = ResultSetConverter.getGroup(result);
                group.setMembers(getMembersOf(group));
//                GroupTaskDAO dao = (GroupTaskDAO)(taskDAOMap.get(GroupTask.class));
//                group.setTopics(getTopicsByGroup(group));
//                group.setTopics(getTopics());
                groups.add(group);
            }
            return filter.doFilter(groups);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * Designed to be used only with getGroups Method
//     */
//    public Set<Topic> getTopicsByGroup(Group group) {
//        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection();
//             PreparedStatement statement = connection.prepareStatement(getTopicsByGroupSQL)) {
//            statement.setString(1, group.getId());
//            ResultSet result = statement.executeQuery();
//            Set<Topic> topics = new LinkedHashSet<>();
//            while (result.next()) {
//                topics.add(ResultSetConverter.getTopic(result));
//            }
//            return topics;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public Set<Topic> getTopics(Filter<Topic> topicFilter) {
        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = connection.prepareStatement(getTopicsSQL)) {
            ResultSet result = statement.executeQuery();
            Set<Topic> topics = new LinkedHashSet<>();
            while (result.next()) {
                topics.add(ResultSetConverter.getTopic(result));
            }
            return topicFilter.doFilter(topics);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void joinGroup(String id, User user) throws SQLException {
        try (Connection conn = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = conn.prepareStatement(joinGroupSQL)) {
            statement.setString(1, user.getId());
            statement.setString(2, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public Set<GroupTask> getGroupTasks(Group group) {
        return null;
    }


    @Override
    public Set<User> getMembersOf(Group group) {
        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection(); PreparedStatement statement = connection.prepareStatement(getMembersOfGroup)) {
            statement.setString(1, group.getId());
            return getMembers(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Set<User> getMembers(PreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        Set<User> members = new HashSet<>();
        while (result.next()) {
            members.add(ResultSetConverter.getUser(result));
        }
        return members;
    }

    @Override
    public Map<User, Date> getUsersCompletedGroupTask(GroupTask task) {
        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection();
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
    public Topic insertTopic(Topic topic) {
        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = connection.prepareStatement(insertTopicSQL)) {
            topic.setId(randomId());
            statement.setString(1, topic.getId());
            statement.setString(2, topic.getName());
            statement.setString(3, topic.getGroupId());
            statement.setDate(4, toSqlDate(topic.getStartDate()));
            statement.execute();
            return topic;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Session storeLogin(String userId, String facebookToken) {
        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = connection.prepareStatement(storeLoginSQL)) {
            String token = randomId();
            String key = randomId();
            statement.setString(1, token);
            statement.setString(2, userId);
            statement.setString(3, encrypt(facebookToken, key));
            statement.execute();
            return new Session(token, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteLogin(String userId) {
        try (Connection connection = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteLoginSQL)) {
            statement.setString(1, userId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new user into the database.
     *
     * @param firstName The first name of the new user
     * @param lastName  The last name of the new user
     */
    @Override
    public User registerUser(String firstName, String lastName, String email, String facebookId, String pictureUrl) throws SQLIntegrityConstraintViolationException {
        try (Connection conn = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = conn.prepareStatement(registerUserSQL)) {
            String token = randomId();
            statement.setString(1, token);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, facebookId);
            statement.setString(6, pictureUrl);
            statement.execute();
            return new User(token, firstName, lastName, email, facebookId, pictureUrl);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User registerUser(User user) throws SQLIntegrityConstraintViolationException {
        return registerUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getFacebookId(), user.getPictureUrl());
    }

    @Override
    public User updateUser(User user) {
        try(Connection conn = sources.get(SOURCE_FLAVOR).getConnection();
            PreparedStatement statement = conn.prepareStatement(updateUserSQL)){
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPictureUrl());
            statement.setString(5, user.getId());
            statement.execute();
            return getUserByFacebookId(user.getFacebookId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String randomId() {
        return UUID.randomUUID().toString();
    }

    private String getHash(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(string.getBytes());
            return new sun.misc.BASE64Encoder().encode(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static SQLDatabaseAccessor getInstance() {
        return instance;
    }

    @Override
    public Group createGroup(Group group) throws SQLIntegrityConstraintViolationException { // TODO change to create by Group
        try (Connection conn = sources.get(SOURCE_FLAVOR).getConnection();
             PreparedStatement statement = conn.prepareStatement(createGroupSQL)) {
            group.setId(randomId());
            statement.setString(1, group.getId());
            statement.setString(2, group.getName());
            statement.setString(3, group.getFacebookId());
            statement.setString(4, group.getPictureUrl());
            statement.execute();
            return group;
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
        try (Connection conn = sources.get(SOURCE_FLAVOR).getConnection(); PreparedStatement statement = conn.prepareStatement(getGroupSQL)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? ResultSetConverter.getGroup(resultSet) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Group getGroupByFacebookId(String facebookId) {
        try (Connection conn = sources.get(SOURCE_FLAVOR).getConnection(); PreparedStatement statement = conn.prepareStatement(getGroupByFacebookIdSQL)) {
            statement.setString(1, facebookId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? ResultSetConverter.getGroup(resultSet) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void insertTask(Task task) {
        taskDAOMap.get(task.getClass()).insertTask(task);
    }

    public String encrypt(String raw, String key) {
        Encryption encryption = Encryption.getDefault(key, "fillersalt", new byte[16]);
        return encryption.encryptOrNull(raw);
    }

    public String decrypt(String encrypted, String key) {
        Encryption encryption = Encryption.getDefault(key, "fillersalt", new byte[16]);
        return encryption.decryptOrNull(encrypted);
    }

    public java.sql.Date toSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

}
