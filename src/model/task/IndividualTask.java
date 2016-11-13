package model.task;

import model.Completable;
import model.User;

import java.util.Date;

/**
 * @author Andi Gu
 */
public class IndividualTask extends Completable {
    private User owner;
    private boolean complete;

    public IndividualTask(long id, String name, User owner, Date dueDate) {
        super(id, name, dueDate);
        this.owner = owner;
        complete = false;
    }

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
}
