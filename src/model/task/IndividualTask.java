package model.task;

import model.User;

import java.util.Date;

/**
 * An individual task belongs to one user and one user only. It may not be shared with others. Only the owner has any
 * form of access to it.
 *
 * @author Andi Gu
 */
public class IndividualTask extends Task {
    private User owner;
    private boolean complete;

    public IndividualTask(String id, String name, User owner, Date dueDate) {
        super(id, name, dueDate);
        this.owner = owner;
        complete = false;
    }

    public IndividualTask() {super();}

    public User getOwner() {
        return owner;
    }

    public boolean isComplete() {
        return complete;
    }

    @Override
    public void complete(User user, Date completed) {
        assert user == owner: "User given is not the owner of the task";
        complete = true;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
