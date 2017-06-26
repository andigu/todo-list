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
import java.util.Map;

/**
 * @author Andi Gu
 */
@WebServlet("/login")
public class LoginServlet extends ApplicationServlet {
    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData) throws IOException {
        System.out.println("login attempt received");

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>();
        Map<String, String> authResponse = converter.cast(requestData, SupportedTypeReference.STRING_MAP);
        try {
            System.out.println(fb.getLongLiveToken(authResponse.get("accessToken")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  responseEntity;
    }
}
