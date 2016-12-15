package database.dao;

import database.ResultSetConverter;
import model.User;
import model.group.Project;
import model.task.ProjectTask;
import model.task.Task;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Andi Gu
 */
public class ProjectTaskDAO extends TaskDAO {
    private static final String getAllProjectTasksSQL = "SELECT * FROM MODEL.PROJECT_TASKS JOIN (SELECT * FROM MODEL.USER_PROJECTS" +
            " NATURAL JOIN MODEL.PROJECTS WHERE USER_ID = ?) AS UPT ON MODEL.PROJECT_TASKS.PROJECT_ID = UPT.PROJECT_ID";
    private static final String insertProjectTask = "INSERT INTO MODEL.PROJECT_TASKS (TASK_ID, PROJECT_ID, TASK_NAME, TASK_DUE_DATE) VALUES (?, ?, ?, ?)";

    public ProjectTaskDAO(DataSource dataSource) {
        super(dataSource);
    }


    public Set<ProjectTask> getAllTasks(User user) {
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
            return projectTasks;
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
        ProjectTask projectTask = (ProjectTask) task;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertProjectTask)) {
            statement.setString(1, projectTask.getId());
            statement.setString(2, projectTask.getProject().getId());
            statement.setString(3, projectTask.getName());
            statement.setDate(4, toSqlDate(projectTask.getDueDate()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
