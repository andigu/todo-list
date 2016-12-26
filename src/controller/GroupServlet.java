package controller;

import controller.json.JsonConstants;
import model.User;
import model.group.Group;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
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
        String cmd = request.getParameter(JsonConstants.CMD);
        User user = getLoggedUser(request);

        switch (cmd) {
            case JsonConstants.ALL_GROUPS_CMD:
                responseEntity.setData(db.getAllGroups());
                break;
            //Get all groups user hasn't joined
            case JsonConstants.AVAILABLE_GROUPS_CMD:
                Set<Group> availableGroups = db.getAllGroups();
                availableGroups.removeAll(db.getGroups(user));
                responseEntity.setData(availableGroups);
                break;
            case JsonConstants.JOIN_GROUP_CMD:
                System.out.println("join group requested");
                System.out.println(request.getParameter(JsonConstants.GROUP_ID));
                db.joinGroup(request.getParameter(JsonConstants.GROUP_ID), user);
            default:
                if (!hasParameter(request, JsonConstants.FILTERS) && user != null) {
                    responseEntity.setData(db.getGroups(user));
                }
                break;
        }

        return responseEntity;
    }
}
