package model;

import com.fasterxml.jackson.annotation.*;

/**
 * All named entities and those that interact [are stored in] the database are identifiable. They have a unique id
 * (unique to the rest of the objects of the same type). For example, all Group objects should have unique id's, but
 * some Projects may have id's that overlap with the id's of the Group objects. It also ensures that Jackson can
 * convert the object into a JSON.
 *
 * @author Andi Gu
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityReference(alwaysAsId = true)
public abstract class Identifiable implements Comparable<Identifiable> {
    private final Long id;
    private String name;

    public Identifiable() {
        id = null;
    }

    public Identifiable(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Identifiable o) {
        assert id != null;
        return id.compareTo(o.getId());
    }
}
