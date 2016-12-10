package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import controller.json.JsonConstant;
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
    void writeResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws JsonProcessingException {
        User user;
        user = db.getUserByLogin(request.getParameter(JsonConstant.USERNAME), request.getParameter(JsonConstant.PASSWORD));
        if (user != null) {
            if (request.getParameter(JsonConstant.STAY_LOGGED).equals("true")) {
                jsonMap.put(JsonConstant.TOKEN, db.storeLogin(user.getId()));
            }
        }

        if (user != null) {
            jsonMap.put(JsonConstant.USER, user);
            request.getSession().setAttribute(JsonConstant.USER_ID, user.getId());
        }
    }
}
