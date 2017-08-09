package model.facebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import model.User;
import model.group.Group;



/**
 * @author Susheel Kona
 */
public class FacebookDataHelper {
    private ObjectMapper mapper = new ObjectMapper();


    public Group getGroup (String json) throws java.io.IOException {
        JsonNode root = mapper.readTree(json);
        return null;
    }

    public User getUser(String json) throws java.io.IOException {
        JsonNode root = mapper.readTree(json);
        return new User(root.path("first_name").asText(), root.path("last_name").asText(), root.path("email").asText(),
                root.path("id").asText(), extractPictureUrl(root.path("picture")));
    }

    private String extractPictureUrl(JsonNode picture) {
        return picture.path("data").path("url").asText();

    }

    private static final FacebookDataHelper instance = new FacebookDataHelper();
    public static FacebookDataHelper getInstance() {
        return instance;
    }
}
