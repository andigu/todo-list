package database;

import model.Completable;
import model.User;
import model.group.Group;
import model.group.Project;
import model.group.Topic;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Andi Gu
 */
public final class ResultSetConverter {
    static User getUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getString("USER_ID"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getString("EMAIL"), resultSet.getString("FACEBOOK_ID"), resultSet.getString("PICTURE_URL"));
    }

    public static Group getGroup(ResultSet resultSet) throws SQLException {
        return new Group(resultSet.getString("GROUP_ID"), resultSet.getString("GROUP_NAME"), resultSet.getString("FACEBOOK_ID"), resultSet.getString("PICTURE_URL"), "");
    }

    public static Topic getTopic(ResultSet rs) throws SQLException{
        return new Topic(rs.getString("TOPIC_ID"), rs.getString("TOPIC_NAME"),rs.getString("GROUP_ID"), rs.getDate("START_DATE"), rs.getDate("END_DATE"), rs.getBoolean("COMPLETED"));
    }

    public static Project getProject(ResultSet resultSet) throws SQLException {
        Project project = new Project(resultSet.getString("PROJECT_ID"), resultSet.getString("PROJECT_NAME"), resultSet.getDate("PROJECT_DUE_DATE"));
        complete(project, resultSet);
        return project;
    }

    public static GroupTask getGroupTask(ResultSet resultSet, Group group) throws SQLException {
        GroupTask groupTask = new GroupTask(resultSet.getString("TASK_ID"), resultSet.getString("TASK_NAME"), resultSet.getDate("TASK_DUE_DATE"), group);
        groupTask.complete(SQLDatabaseAccessor.getInstance().getUsersCompletedGroupTask(groupTask));
        return groupTask;
    }

    public static ProjectTask getProjectTask(ResultSet resultSet, Project project) throws SQLException {
        ProjectTask task = new ProjectTask(resultSet.getString("TASK_ID"), resultSet.getString("TASK_NAME"), resultSet.getDate("TASK_DUE_DATE"), project);
        complete(task, resultSet);
        return task;
    }

    public static IndividualTask getIndividualTask(ResultSet resultSet, User user) throws SQLException {
        IndividualTask task = new IndividualTask(resultSet.getString("TASK_ID"), resultSet.getString("TASK_NAME"), user, resultSet.getDate("TASK_DUE_DATE"));
        complete(task, resultSet);
        return task;
    }

    private static void complete(Completable completable, ResultSet resultSet) throws SQLException {
        if (resultSet.getBoolean("COMPLETED")) {
            completable.complete(SQLDatabaseAccessor.getInstance().getUserById(resultSet.getString("USER_COMPLETED_ID")), resultSet.getDate("DATE_COMPLETED"));
        }
    }

}
