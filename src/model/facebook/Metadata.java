package model.facebook;

import java.util.Map;

/**
 * Holder for node metadata that comes from the Facebook Graph API
 *
 * @author Susheel Kona
 */
public class Metadata {
    private String type;
    private Map<String, String> connections;

    public String getType() {
        return type;
    }

    public Map<String, String> getConnections() {
        return connections;
    }
}
