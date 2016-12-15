package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import controller.json.Error;
import controller.json.JsonConstants;
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
            User user = db.registerUser(request.getParameter(JsonConstants.USERNAME), request.getParameter(JsonConstants.PASSWORD),
                    request.getParameter(JsonConstants.FIRST_NAME), request.getParameter(JsonConstants.LAST_NAME));
            jsonMap.put(JsonConstants.USER, user);
        } catch (SQLIntegrityConstraintViolationException e) {
            writeError(new Error(JsonConstants.DUPLICATE_KEY_ERROR), jsonMap);
        }
    }

}
