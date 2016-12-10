package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import controller.json.JsonConstant;
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
    void writeResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws JsonProcessingException {
        switch (request.getParameter(JsonConstant.CMD)) {
            case JsonConstant.PING_CMD:
                writeStatus(new Status(JsonConstant.LOGGED_IN_STATUS, getLoggedUser(request) != null), jsonMap);
                break;
            case JsonConstant.USER_INF_CMD:
                if (getLoggedUser(request) != null) {
                    jsonMap.put(JsonConstant.USER, getLoggedUser(request));
                }
                break;
        }
    }
}
