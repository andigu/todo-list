package controller;

import controller.json.Error;
import database.filter.GroupFilter;
import model.Session;
import model.group.Group;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

/**
 * @author Andi Gu
 * @author Susheel Kona
 */

@WebServlet("/groups")
public class GroupServlet extends ApplicationServlet {
    @Override
    public ResponseEntity<?> processGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseEntity<Set<Group>> responseEntity = new ResponseEntity<>();
        responseEntity.setData(db.getGroups(getLoggedUser(request), addConstraints(new GroupFilter(), request)));
        return responseEntity;
    }

    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData, Session session) throws IOException {
        ResponseEntity<Group> responseEntity = new ResponseEntity<>();
        String facebookAccessToken = getFacebookToken(session);
        try {
            responseEntity.setData(fb.getGroupByFacebookId(facebookAccessToken, requestData.get("facebookId").toString()));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            responseEntity.setError(new Error(e.getMessage()));
        }
        return responseEntity;
    }
}
