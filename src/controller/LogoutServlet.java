package controller;

import controller.json.JsonConstants;
import controller.json.Status;
import model.Session;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Andi Gu
 */

@WebServlet("/logout")
public class LogoutServlet extends ApplicationServlet {
    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData, Session session) throws IOException {
        ResponseEntity responseEntity = new ResponseEntity();
        deleteCookie(request, JsonConstants.TOKEN, response);
        request.getSession().removeAttribute(JsonConstants.USER_ID);
        responseEntity.setStatus(new Status("success", getLoggedUser(request) == null));
        return responseEntity;
    }
}
