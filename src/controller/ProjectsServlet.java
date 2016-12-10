package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import controller.json.JsonConstant;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Andi Gu
 */

@WebServlet("/projects")
public class ProjectsServlet extends ApplicationServlet {
    @Override
    void writeResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws JsonProcessingException {
        User user = getLoggedUser(request);
        if (!hasParameter(request, JsonConstant.FILTERS)) {
            if (user != null) {
                jsonMap.put(JsonConstant.PROJECTS, db.getProjects(user));
            }
        }
    }
}

