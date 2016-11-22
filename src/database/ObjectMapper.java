package database;

import model.Completable;
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
        Project project = new Project(resultSet.getLong("PROJECT_ID"), resultSet.getString("GROUP_NAME"), resultSet.getDate("DUE_DATE"));
        complete(project, resultSet);
        return project;
    }

    static GroupTask getGroupTask(ResultSet resultSet, Group group) throws SQLException {
        GroupTask groupTask = new GroupTask(resultSet.getLong("TASK_ID"), resultSet.getString("TASK_NAME"), resultSet.getDate("DUE_DATE"), group);
        groupTask.complete(DerbyDatabaseAccessor.getInstance().getUsersCompletedGroupTask(groupTask));
        return groupTask;
    }

    static ProjectTask getProjectTask(ResultSet resultSet, Project project) throws SQLException {
        ProjectTask task = new ProjectTask(resultSet.getLong("PROJECT_TASK_ID"), resultSet.getString("TASK_NAME"), resultSet.getDate("DUE_DATE"), project);
        complete(task, resultSet);
        return task;
    }

    static IndividualTask getIndividualTask(ResultSet resultSet, User user) throws SQLException {
        IndividualTask task = new IndividualTask(resultSet.getLong("INDIVIDUAL_TASK_ID"), resultSet.getString("TASK_NAME"), user, resultSet.getDate("DUE_DATE"));
        complete(task, resultSet);
        return task;
    }

    private static void complete(Completable completable, ResultSet resultSet) throws SQLException {
        if (resultSet.getBoolean("COMPLETED")) {
            completable.complete(DerbyDatabaseAccessor.getInstance().getUserById(resultSet.getLong("USER_COMPLETED_ID")), resultSet.getDate("DATE_COMPLETED"));
        }
    }

}
