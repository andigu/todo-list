package model;

/**
 * @author Andi Gu
 * Class to ensure all objects that interact with the database have an id linked to the one given to it in the db
 * Additionally, ensures Jackson can convert the object into a JSON
 */
public abstract class Identifiable {
    private final long id;
    private String name;

    public Identifiable(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
