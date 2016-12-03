package controller;

import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Andi Gu
 */
@WebServlet("/login")
public class LoginServlet extends ApplicationServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("json/application");
        Map<String, Object> jsonMap = new HashMap<>();
        User user;
        if (hasParameter(req, "token")) {
            user = db.getUserByToken(Long.parseLong(req.getParameter("token")));
        }
        else {
            user = db.getUserByLogin(req.getParameter("username"), req.getParameter("password"));
        }
        jsonMap.put("user", user);
        if (user != null) {
            if (req.getParameter("stay-logged").equals("on")) {
                jsonMap.put("token", db.storeLogin(user.getId()));
            }
        }
        resp.getWriter().write(converter.toJson(jsonMap));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
