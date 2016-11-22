package database;

import model.User;
import model.group.Group;
import model.group.Project;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Andi Gu
 */
final class ObjectMapper {
    static User getUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("USER_ID"), resultSet.getString("USERNAME"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"));
    }

    static Group getGroup(ResultSet resultSet) throws SQLException {
        return new Group(resultSet.getLong("GROUP_ID"), resultSet.getString("GROUP_NAME"));
    }

    static Project getProject(ResultSet resultSet) throws SQLException {
        return new Project(resultSet.getLong("PROJECT_ID"), resultSet.getString("GROUP_NAME"), resultSet.getDate("DUE_DATE"));
    }

    static GroupTask getGroupTask(ResultSet resultSet, Group group) throws SQLException {
        return new GroupTask(resultSet.getLong("GROUP_TASK_ID"), resultSet.getString("GROUP_TASK_NAME"), resultSet.getDate("DUE_DATE"), group);
    }

    static ProjectTask getProjectTask(ResultSet resultSet, Project project) throws SQLException {
        return new ProjectTask(resultSet.getLong("PROJECT_TASK_ID"), resultSet.getString("PROJECT_TASK_NAME"), resultSet.getDate("DUE_DATE"), project);
    }

    static IndividualTask getIndividualTask(ResultSet resultSet, User user) throws SQLException {
        return new IndividualTask(resultSet.getLong("INDIVIDUAL_TASK_ID"), resultSet.getString("NAME"), user, resultSet.getDate("DUE_DATE"));
    }
}
