package controller;

import controller.json.JsonConstants;
import controller.json.SupportedTypeReference;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Andi Gu
 */
@WebServlet("/login")
public class LoginServlet extends ApplicationServlet {
    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData) throws IOException {
        ResponseEntity<User> responseEntity = new ResponseEntity<>();
        Map<String, String> userData = converter.cast(requestData.get(JsonConstants.USER), SupportedTypeReference.STRING_MAP);
        User user = db.getUserByLogin(userData.get(JsonConstants.USERNAME), userData.get(JsonConstants.PASSWORD));
        responseEntity.setData(user);
        if (user != null) {
            if (converter.cast(requestData.get(JsonConstants.STAY_LOGGED), SupportedTypeReference.BOOLEAN)) {
                response.addCookie(new Cookie(JsonConstants.TOKEN, db.storeLogin(user.getId())));
            }
            request.getSession().setAttribute(JsonConstants.USER_ID, user.getId());
        }
        return responseEntity;
    }
}
