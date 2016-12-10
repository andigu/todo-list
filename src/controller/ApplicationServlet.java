package controller;

import controller.json.Error;
import controller.json.JsonConstant;
import controller.json.JsonConverter;
import controller.json.Status;
import database.DatabaseAccessor;
import database.DerbyDatabaseAccessor;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andi Gu
 */
public abstract class ApplicationServlet extends HttpServlet {
    DatabaseAccessor db = DerbyDatabaseAccessor.getInstance();
    JsonConverter converter = JsonConverter.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(JsonConstant.JSON_CONTENT_TYPE);
        Map<String, Object> jsonMap = new HashMap<>();
        writeResponse(request, jsonMap);
        resp.getWriter().write(converter.toJson(jsonMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    void writeError(Error error, Map<String, Object> jsonMap) {
        jsonMap.put(JsonConstant.ERROR, error);
    }

    void writeStatus(Status status, Map<String, Object> jsonMap) {
        jsonMap.put(JsonConstant.STATUS, status);
    }

    abstract void writeResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws IOException;

    boolean hasParameter(HttpServletRequest request, String parameter) {
        return request.getParameterMap().containsKey(parameter);
    }

    User getLoggedUser(HttpServletRequest request) {
        User sessionUser = getSessionUser(request), cookieUser = getCookieUser(request);
        if (sessionUser != null) {
            return sessionUser;
        } else {
            return cookieUser;
        }
    }

    private User getSessionUser(HttpServletRequest request) {
        Object userId = request.getSession().getAttribute(JsonConstant.USER_ID);
        if (userId != null) {
            return db.getUserById(userId.toString());
        } else {
            return null;
        }
    }

    private User getCookieUser(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(JsonConstant.TOKEN)) {
                return db.getUserByToken(cookie.getValue());
            }
        }
        return null;
    }
}
