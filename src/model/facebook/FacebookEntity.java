package model.facebook;

import model.Identifiable;

/**
 * A model for all entities that come from the Facebook Graph API.
 *
 * @author Susheel Kona
 */
public abstract class FacebookEntity extends Identifiable {
    private String facebookId;
    private String pictureUrl;

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

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        return ((this.getId() != null) && (((Identifiable)(o)).getId() != null)) ? super.equals(o)
                : ((FacebookEntity)(o)).getFacebookId().matches(this.getFacebookId());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (facebookId != null ? facebookId.hashCode() : 0);
        return result;
    }
}
