package controller;

import controller.json.JsonConstants;
import model.User;
import model.group.Project;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Andi Gu
 */

@WebServlet("/projects")
public class ProjectsServlet extends ApplicationServlet {
    @Override
    public ResponseEntity<?> processGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseEntity<Set<Project>> responseEntity = new ResponseEntity<>();
        User user = getLoggedUser(request);
        if (!hasParameter(request, JsonConstants.FILTERS) && user != null) {
            responseEntity.setData(db.getProjects(user));
        }
        System.out.println(response);
        return responseEntity;
    }
}

