package model.facebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import model.User;
import model.group.Group;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * @author Susheel Kona
 */
public class FacebookDataHelper {
    private ObjectMapper mapper = new ObjectMapper();


    public Group getGroup (String json) throws IOException {
        JsonNode root = mapper.readTree(json);
        Group group = new Group(root.path("id").asText(), root.path("name").asText(), root.path("privacy").asText(),
                extractPictureUrl(root.path("picture")));
        JsonNode membersNode = root.path("members").path("data");
        Set<User> members = new HashSet<User>();
        for(JsonNode node: membersNode) {
            members.add(new User(node.path("name").asText()){{
                setFacebookId(node.path("id").asText());
            }});
        }
        group.setMembers(members);
        return group;
    }

    public User getUser(String json) throws IOException {
        JsonNode root = mapper.readTree(json);
        return new User(root.path("first_name").asText(), root.path("last_name").asText(), root.path("email").asText(),
                root.path("id").asText(), extractPictureUrl(root.path("picture")));
    }

    public FacebookException getException(String json) throws IOException {
        JsonNode root = mapper.readTree(json);
        int errorCode = root.path("error").path("code").asInt();
        return new FacebookException(errorCode);

    }

    private String extractPictureUrl(JsonNode picture) {
        return picture.path("data").path("url").asText();

    }

    private static final FacebookDataHelper instance = new FacebookDataHelper();
    public static FacebookDataHelper getInstance() {
        return instance;
    }
}
