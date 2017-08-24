package controller;

import controller.json.Error;
import database.filter.GroupFilter;
import model.Session;
import model.User;
import model.facebook.FacebookException;
import model.group.Group;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLIntegrityConstraintViolationException;
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

    /**
     * Processes requests to join groups
     *
     * @return
     * @throws IOException
     */
    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData, Session session) throws IOException {
        ResponseEntity<Group> responseEntity = new ResponseEntity<>();
        String facebookAccessToken = getFacebookToken(session);
        User user = getLoggedUser(session);
        try {
            Group facebookGroup = fb.getGroupByFacebookId(facebookAccessToken, requestData.get("facebookId").toString());
            Group dbGroup = db.getGroupByFacebookId(facebookGroup.getFacebookId());
            System.out.println((dbGroup == null)+" dbgroupisnull");

            // Check if the user is a member of the group on facebook
            if (!(facebookGroup.getMembers().contains(user))) throw new FacebookException(100);

            if (dbGroup == null) dbGroup = db.createGroup(facebookGroup);

            db.joinGroup(dbGroup.getId(), user);
            responseEntity.setData(dbGroup);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            responseEntity.setError(new Error(e.getMessage()));
        }
//        catch (SQLIntegrityConstraintViolationException e) {
//            responseEntity.setError(new Error("You are already a member of that group"));
//        }
        return responseEntity;
    }
}
