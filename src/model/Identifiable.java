package model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
    private String id;
    private String name;

    public Identifiable() {
        this.id = UUID.randomUUID().toString();
    }

    public Identifiable(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public Identifiable(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    // Only useful for fb
    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object obj) {
        return (((Identifiable)(obj)).getId().matches(id));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
