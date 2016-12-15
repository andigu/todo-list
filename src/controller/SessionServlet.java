package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import controller.json.JsonConstants;
import controller.json.Status;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Just for front end to ping if a session for the user exists
 *
 * @author Andi Gu
 */

@WebServlet("/sessions")
public class SessionServlet extends ApplicationServlet {
    @Override
    public void writeGetResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws JsonProcessingException {
        switch (request.getParameter(JsonConstants.CMD)) {
            case JsonConstants.PING_CMD:
                writeStatus(new Status(JsonConstants.LOGGED_IN_STATUS, getLoggedUser(request) != null), jsonMap);
                break;
            case JsonConstants.USER_INF_CMD:
                if (getLoggedUser(request) != null) {
                    jsonMap.put(JsonConstants.USER, getLoggedUser(request));
                }
                break;
        }
    }
}
