package model;

import java.util.Date;

/**
 * @author Andi Gu
 */
public abstract class Completable extends Identifiable {
    private Date dueDate;

    public Completable(long id, String name, Date dueDate) {
        super(id, name);
        this.dueDate = dueDate;
    }

    public abstract void complete(User user, Date completed);

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
