package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import controller.json.JsonConstants;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Andi Gu
 */

@WebServlet("/groups")
public class GroupServlet extends ApplicationServlet {
    @Override
    public void writeGetResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws JsonProcessingException {
        User user = getLoggedUser(request);
        if (!hasParameter(request, JsonConstants.FILTERS)) {
            if (user != null) {
                jsonMap.put(JsonConstants.GROUPS, db.getGroups(user));
            }
        }
    }
}
