package controller;

import controller.json.JsonConverter;
import database.DatabaseAccessor;
import database.DerbyDatabaseAccessor;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Andi Gu
 */
@WebServlet("/login")
public class LoginServlet extends ApplicationServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        resp.setContentType("json/application");
        User user = db.getUserByLogin(username, password);
        if (req.getParameter("stay-logged").equals("checked") && user != null) {

        }
        resp.getWriter().write(converter.toJson(user));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
