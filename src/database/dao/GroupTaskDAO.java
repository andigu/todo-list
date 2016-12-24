package database.dao;

import database.ResultSetConverter;
import model.User;
import model.group.Group;
import model.task.GroupTask;
import model.task.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Andi Gu
 */
public class GroupTaskDAO extends TaskDAO {
    private static final String getAllGroupTasksSQL = "SELECT * FROM MODEL.GROUP_TASKS JOIN (SELECT * FROM MODEL.USER_GROUPS" +
            " NATURAL JOIN MODEL.GROUPS WHERE USER_ID = ?) AS UGT ON MODEL.GROUP_TASKS.GROUP_ID = UGT.GROUP_ID";
    private static final String getGroupTasksSQL = "SELECT * FROM MODEL.GROUP_TASKS WHERE GROUP_ID = ?";
    private static final String insertGroupTask = "INSERT INTO MODEL.GROUP_TASKS (TASK_ID, GROUP_ID, TASK_NAME, TASK_DUE_DATE) VALUES (?, ?, ?, ?)";

    public GroupTaskDAO(javax.sql.DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Set<GroupTask> getAllTasks(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAllGroupTasksSQL)) {
            statement.setString(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<GroupTask> groupTasks = new HashSet<>();
            Map<String, Group> groupMap = new HashMap<>();
            String id;
            while (resultSet.next()) {
                id = resultSet.getString("GROUP_ID");
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

    public Set<GroupTask> getTasksByGroup(Group group) {
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
    public <T extends Task> void completeTask(T task, Date dateCompleted) {

    }

    @Override
    public void insertTask(Task task) {
        GroupTask groupTask = (GroupTask) task;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertGroupTask)) {
            statement.setString(1, groupTask.getId());
            statement.setString(2, groupTask.getGroup().getId());
            statement.setString(3, groupTask.getName());
            statement.setDate(4, toSqlDate(groupTask.getDueDate()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
