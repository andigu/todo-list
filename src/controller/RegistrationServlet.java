package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import controller.json.Error;
import controller.json.JsonConstant;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

/**
 * Requests to register new users in the database are processed in this servlet
 *
 * @author Susheel Kona
 */
@WebServlet("/register")
public class RegistrationServlet extends ApplicationServlet {
    @Override
    public void writeGetResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws JsonProcessingException {
        //TODO check for null values
        try {
            User user = db.registerUser(request.getParameter(JsonConstant.USERNAME), request.getParameter(JsonConstant.PASSWORD),
                    request.getParameter(JsonConstant.FIRST_NAME), request.getParameter(JsonConstant.LAST_NAME));
            jsonMap.put(JsonConstant.USER, user);
        } catch (SQLIntegrityConstraintViolationException e) {
            writeError(new Error(JsonConstant.DUPLICATE_KEY_ERROR), jsonMap);
        }
    }

}
