package controller;

import controller.json.JsonConverter;
import database.DatabaseAccessor;
import database.DerbyDatabaseAccessor;
import model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
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

    User getLoggedUser(HttpServletRequest request) {
        User session = getSessionUser(request), cookie = getCookieUser(request);
        if (session == null && cookie == null) {
            return null;
        } else {
            return session == null ? cookie : session;
        }
    }

    private User getSessionUser(HttpServletRequest request) {
        if (request.getSession().getAttribute("user-id") != null) {
            return db.getUserById(request.getSession().getAttribute("user-id").toString());
        } else {
            return null;
        }
    }

    private User getCookieUser(HttpServletRequest request) {
        int index = -1;
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("token")) {
                index = i;
            }
        }

        if (index >= 0) {
            return db.getUserByToken(request.getCookies()[index].getValue());
        } else {
            return null;
        }
    }
}
