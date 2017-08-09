package model.facebook;

import model.Identifiable;

import javax.persistence.Id;
import java.util.Map;

/**
 * A model for all entities that come from the Facebook Graph API.
 *
 * @author Susheel Kona
 */
public abstract class FacebookEntity extends Identifiable {
    private String facebookId;
//    private Picture picture;

    // Default constructors
    public FacebookEntity() {
        super();
    }

    public FacebookEntity(String name) {
        super(name);
    }

    public FacebookEntity(String id, String name) {
        super(id, name);
    }

    public String getFacebookId() {
        return facebookId;
    }

    @Override
    public boolean equals(Object o) {
        return ((this.getId() != null) && (((Identifiable)(o)).getId() != null)) ? super.equals(o) : (this.getFacebookId() == ((FacebookEntity)(o)).getFacebookId());
    }

}

//class Picture {
//    private Map<String, String> data;
//    public Map<String, String> getData() {
//        return data;
//    }
//}
//
