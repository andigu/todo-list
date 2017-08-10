package controller;

import controller.json.Error;
import controller.json.JsonConstants;
import controller.json.SupportedTypeReference;
import model.Session;
import model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Susheel Kona, Andi Gu
 */
@WebServlet("/login")
public class LoginServlet extends ApplicationServlet {

    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData, Session session) throws IOException {
        System.out.println("login attempt received");

        ResponseEntity<LoginResponse> responseEntity = new ResponseEntity<>();
        Map<String, String> authResponse = converter.cast(requestData, SupportedTypeReference.STRING_MAP);

        try {
            String longLiveToken = longLiveToken = fb.getLongLiveToken(authResponse.get(JsonConstants.ACCESS_TOKEN));
            User databaseUser = db.getUserByFacebookId(authResponse.get(JsonConstants.FACEBOOK_USER_ID)), facebookUser = fb.getUserByAccessToken(longLiveToken);
            if (databaseUser == null) {
                databaseUser = db.registerUser(facebookUser);
            } else {
                db.deleteLogin(databaseUser.getId());
                System.out.println("Updating user");
                facebookUser.setId(databaseUser.getId());
                databaseUser = db.updateUser(facebookUser);
            }
            session = db.storeLogin(databaseUser.getId(), longLiveToken);
            System.out.println(databaseUser);
            System.out.println(facebookUser);
            responseEntity.setData(new LoginResponse(databaseUser, session));
            request.getSession().setAttribute(JsonConstants.USER_ID, databaseUser.getId());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            responseEntity.setError(new Error(e.getMessage()));
        }

        return responseEntity;
    }


}

class LoginResponse {
    User user;
    Session session;

    public LoginResponse(User user, Session session) {
        this.user = user;
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public Session getSession() {
        return session;
    }
}