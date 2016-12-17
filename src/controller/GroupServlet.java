package controller;

import controller.json.JsonConstants;
import model.User;
import model.group.Group;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Andi Gu
 */

@WebServlet("/groups")
public class GroupServlet extends ApplicationServlet {
    @Override
    public ResponseEntity<?> processGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseEntity<Set<Group>> responseEntity = new ResponseEntity<>();
        User user = getLoggedUser(request);
        if (!hasParameter(request, JsonConstants.FILTERS) && user != null) {
            responseEntity.setData(db.getGroups(user));
        }
        return responseEntity;
    }
}
