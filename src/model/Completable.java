package model;

import java.util.Date;

/**
 * A completable object implies that there is a due date, and that the object can be marked as complete. Data is
 * kept on which user marked it as complete, and when.
 *
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
