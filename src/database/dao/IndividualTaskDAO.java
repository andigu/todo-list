package database.dao;

import database.ResultSetConverter;
import model.User;
import model.task.IndividualTask;
import model.task.Task;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andi Gu
 */
public class IndividualTaskDAO extends TaskDAO {
    private static final String getAllIndividualTasksSQL = "SELECT * FROM MODEL.INDIVIDUAL_TASKS WHERE USER_ID = ?";
    private static final String insertIndividualTask = "INSERT INTO MODEL.INDIVIDUAL_TASKS (TASK_ID, USER_ID, TASK_NAME, TASK_DUE_DATE) VALUES (?, ?, ?, ?)";

    public IndividualTaskDAO(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    public Set<IndividualTask> getAllTasks(User user) {
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
    public <T extends Task> void completeTask(T task, Date dateCompleted) {

    }

    @Override
    public void insertTask(Task task) {
        IndividualTask individualTask = (IndividualTask) task;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertIndividualTask)) {
            statement.setString(1, individualTask.getId());
            statement.setString(2, individualTask.getOwner().getId());
            statement.setString(3, individualTask.getName());
            statement.setDate(4, toSqlDate(individualTask.getDueDate()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
