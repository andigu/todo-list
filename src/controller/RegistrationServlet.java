package controller;

import controller.json.Error;
import controller.json.ErrorType;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Requests to register new users in the database are processed in this servlet
 *
 * @author Susheel Kona
 */
@WebServlet("/register")
public class RegistrationServlet extends ApplicationServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("json/application");
        //TODO check for null values
        log(req.getParameter("username"));
        try {
            resp.getWriter().write(converter.toJson(db.registerUser(req.getParameter("username"), req.getParameter("password"),
                    req.getParameter("first-name"), req.getParameter("last-name"))));
        } catch (SQLIntegrityConstraintViolationException e) {
            resp.getWriter().write(converter.toJson(new Error(ErrorType.DuplicateKey)));
        }
    }
}
