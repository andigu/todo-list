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
@WebServlet("/login")
public class LoginServlet extends ApplicationServlet {
    @Override
    public void writeGetResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws JsonProcessingException {
        User user;
        user = db.getUserByLogin(request.getParameter(JsonConstants.USERNAME), request.getParameter(JsonConstants.PASSWORD));
        if (user != null) {
            if (request.getParameter(JsonConstants.STAY_LOGGED).equals("true")) {
                jsonMap.put(JsonConstants.TOKEN, db.storeLogin(user.getId()));
            }
        }

        if (user != null) {
            jsonMap.put(JsonConstants.USER, user);
            request.getSession().setAttribute(JsonConstants.USER_ID, user.getId());
        }
    }
}
