package controller;

import controller.json.JsonConverter;
import database.DatabaseAccessor;
import database.DerbyDatabaseAccessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andi Gu
 */
public abstract class ApplicationServlet extends HttpServlet {
    DatabaseAccessor db = DerbyDatabaseAccessor.getInstance();
    JsonConverter converter = JsonConverter.getInstance();

    boolean hasParameter(HttpServletRequest request, String parameter) {
        return request.getParameterMap().containsKey(parameter);
    }

    String getSessionUserId(HttpServletRequest request) {
        if (request.getSession().getAttribute("user-id") != null) {
            return request.getSession().getAttribute("user-id").toString();
        }
        else {
            return null;
        }
    }
}
