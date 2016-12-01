package model.task;

import model.Completable;

import java.util.Date;

/** Wrapper for cases where it is necessary to hold an array of tasks of unspecified type.
 *
 * @author Andi Gu
 */
public abstract class Task extends Completable{
    Task(long id, String name, Date dueDate) {
        super(id, name, dueDate);
    }
}
