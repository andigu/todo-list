package controller;

import controller.json.JsonConstants;
import controller.json.SupportedTypeReference;
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
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData) throws IOException {
        System.out.println("login attempt received");

        ResponseEntity<LoginResponse> responseEntity = new ResponseEntity<>();
        Map<String, String> authResponse = converter.cast(requestData, SupportedTypeReference.STRING_MAP);

        String longLiveToken="";
        try {
            longLiveToken = fb.getLongLiveToken(authResponse.get("accessToken"));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }

        User user = db.getUserByFacebookId(authResponse.get("userID"));
        if (user == null) {
            user = fb.getUserByAccessToken(longLiveToken);
            try {
                user = db.registerUser(user);
            } catch (SQLIntegrityConstraintViolationException e) {
                e.printStackTrace();
            }
        } else {
            // TODO Update user, delete existing sessions
        }

        //TODO add session

        responseEntity.setData(new LoginResponse(user, "token", "key"));
        return  responseEntity;
    }
}

class LoginResponse {
    User user;
    String token;
    String encryptionKey;

    public LoginResponse(User user, String token, String encryptionKey) {
        this.user = user;
        this.token = token;
        this.encryptionKey = encryptionKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
}