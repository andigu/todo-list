package controller;

import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            user = db.getUserByToken(req.getParameter("token"));
        }
        else {
            user = db.getUserByLogin(req.getParameter("username"), req.getParameter("password"));
            if (user != null) {
                if (req.getParameter("stay-logged").equals("true")) {
                    jsonMap.put("token", db.storeLogin(user.getId()));
                }

            }
        }
        if (user != null) {
            jsonMap.put("user", user);
            req.getSession().setAttribute("user-id", user.getId());
        }
        resp.getWriter().write(converter.toJson(jsonMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
