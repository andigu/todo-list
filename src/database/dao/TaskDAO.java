package database.dao;

import model.User;
import model.task.Task;

import javax.sql.DataSource;
import java.util.Date;
import java.util.Set;

/**
 * @author Andi Gu
 */
public abstract class TaskDAO {
    final DataSource dataSource;

    public TaskDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public abstract Set<? extends Task> getAllTasks(User user);

    public abstract <T extends Task> void completeTask (T task, Date dateCompleted);

    public abstract <T extends Task> void insertTask(T task);

    public java.sql.Date toSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }
}
