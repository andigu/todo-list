package controller;

import database.filter.GroupFilter;
import model.group.Group;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
}
