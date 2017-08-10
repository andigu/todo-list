package controller;

import controller.json.JsonConstants;
import controller.json.StateConverter;
import controller.json.SupportedTypeReference;
import database.DatabaseAccessor;
import database.SQLDatabaseAccessor;
import database.filter.Filter;
import database.filter.FilterType;
import model.Session;
import model.User;
import model.facebook.FacebookService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * @author Andi Gu
 */
public abstract class ApplicationServlet extends HttpServlet {
    DatabaseAccessor db = SQLDatabaseAccessor.getInstance();
    StateConverter converter = StateConverter.getInstance();
    FacebookService fb = FacebookService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(JsonConstants.JSON_CONTENT_TYPE);
        response.getWriter().write(converter.toJson(processGetRequest(request, response)));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(JsonConstants.JSON_CONTENT_TYPE);
        Map<String, Object> requestData = converter.fromJson(requestDataToString(request), SupportedTypeReference.OBJECT_MAP);
        Session session = null;
        try {
            session = new Session(requestData.get("loginToken").toString(), requestData.get("facebookTokenKey").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.getWriter().write(converter.toJson(processPostResponse(request, response, requestData, session)));
    }

    public ResponseEntity<?> processGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return new ResponseEntity<>();
    }

    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData, Session session) throws IOException {
        return new ResponseEntity<>();
    }

    // Only works for get requests?
    User getLoggedUser(HttpServletRequest request) {
        User user = db.getUserByToken(request.getParameter("loginToken"));
        return user;
    }

//    String getFacebookToken(HttpServletRequest request) {
//        System.out.println("logintoken: "+ request.getParameter("loginToken"));
//        System.out.println("map: "+ request.getParameterMap());
//        return db.getFacebookTokenBySession(new Session(request.getParameter("loginToken"), request.getParameter("facebookTokenKey")));
//    }

    // Get requests
    String getFacebookToken(HttpServletRequest request) {
        return getFacebookToken(new Session(request.getParameter("loginToken"), request.getParameter("facebookTokenKey")));
    }

    // Post requests
    String getFacebookToken(Session session) {
        return db.getFacebookTokenBySession(session);
    }


    private String requestDataToString(HttpServletRequest request) throws IOException {
        String data = "";
        try (BufferedReader buffer = request.getReader()) {
            String temp = buffer.readLine();
            while (temp != null) {
                data += temp;
                temp = buffer.readLine();
            }
        }
        return data;
    }

    boolean hasParameter(HttpServletRequest request, String parameter) {
        return request.getParameter(parameter) != null;
    }

    void deleteCookie(HttpServletRequest request, String name, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                cookie.setValue(null);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    <T> Filter<T> addConstraints(Filter<T> filter, HttpServletRequest request) {
        if (filter != null) {
            for (String key : request.getParameterMap().keySet()) {
                FilterType filterType = FilterType.fromValue(key);
                if (filterType != null) {
                    filter.addConstraint(filterType, request.getParameter(key));
                }
            }
        }
        return filter;
    }
}
