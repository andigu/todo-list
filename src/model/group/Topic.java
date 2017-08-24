package model.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import model.Identifiable;

import java.util.Date;

/**
 * Analogous to a unit or a topic of a course
 *
 * @author Susheel Kona
 */
public class Topic extends Identifiable {
    private String groupId;
    private Date startDate;
    private Date endDate;
    private boolean completed;



    public Topic(String id, String name, String groupId, Date startDate, Date endDate, boolean completed) {
        super(id, name);
        this.groupId = groupId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
    }

    public Topic() {

    }

    public String getGroupId() {
        return groupId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isCompleted() {
        return completed;
    }
}
