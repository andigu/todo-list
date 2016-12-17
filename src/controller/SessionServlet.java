package controller;

import controller.json.JsonConstants;
import controller.json.Status;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Just for front end to ping if a session for the user exists
 *
 * @author Andi Gu
 */

@WebServlet("/sessions")
public class SessionServlet extends ApplicationServlet {
    @Override
    public ResponseEntity<?> processGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseEntity<User> responseEntity = new ResponseEntity<>();
        String cmd = request.getParameter(JsonConstants.CMD);
        switch (cmd) {
            case JsonConstants.PING_CMD:
                responseEntity.setStatus(new Status(JsonConstants.LOGGED_IN_STATUS, getLoggedUser(request) != null));
                break;
            case JsonConstants.USER_INF_CMD:
                responseEntity.setData(getLoggedUser(request));
                break;
        }
        return responseEntity;
    }
}
