package controller;

import controller.json.Error;
import controller.json.JsonConstants;
import controller.json.SupportedTypeReference;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

/**
 * Requests to register new users in the database are processed in this servlet
 *
 * @author Susheel Kona
 */
@WebServlet("/register")
public class RegistrationServlet extends ApplicationServlet {
    /**
     * @deprecated Security Reasons
     */
    @Override
    public ResponseEntity<?> processGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseEntity<User> responseEntity = new ResponseEntity<>();
        Map<String, String> registerInf = converter.cast(request.getParameter(JsonConstants.USER_INF), SupportedTypeReference.STRING_MAP);
        try {
            User user = db.registerUser(registerInf.get(JsonConstants.USERNAME), registerInf.get(JsonConstants.PASSWORD),
                    registerInf.get(JsonConstants.FIRST_NAME), registerInf.get(JsonConstants.LAST_NAME));
            responseEntity.setData(user);

        } catch (SQLIntegrityConstraintViolationException e) {
            responseEntity.setError(new Error(JsonConstants.DUPLICATE_KEY_ERROR));
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData) throws IOException {
        ResponseEntity<User> responseEntity = new ResponseEntity<>();
        Map<String, String> registerInf = converter.cast(requestData.get(JsonConstants.USER), SupportedTypeReference.STRING_MAP);
        //TODO handle null and invalid values here
        try {
            User user = db.registerUser(registerInf.get(JsonConstants.USERNAME), registerInf.get(JsonConstants.PASSWORD),
                    registerInf.get(JsonConstants.FIRST_NAME), registerInf.get(JsonConstants.LAST_NAME));
            responseEntity.setData(user);
            request.getSession().setAttribute(JsonConstants.USER_ID, user.getId());

        } catch (SQLIntegrityConstraintViolationException e) {
            responseEntity.setError(new Error(JsonConstants.DUPLICATE_KEY_ERROR));
        }

        return responseEntity;
    }
}
