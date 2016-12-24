package model.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import model.Completable;

import java.util.Date;

/**
 * Wrapper for cases where it is necessary to hold an array of tasks of unspecified type.
 *
 * @author Andi Gu
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Task extends Completable {
    Task(String id, String name, Date dueDate) {
        super(id, name, dueDate);
    }

    Task() {super();}
}
