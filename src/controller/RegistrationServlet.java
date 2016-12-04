package controller;

import database.DerbyDatabaseAccessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 *  Requests to register new users in the database are processed in this servlet
 *  @author Susheel Kona
 *  @since 12/2/2016
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = "/register")
public class RegistrationServlet extends ApplicationServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("json/application");
        //TODO check for null values
        log(req.getParameter("username"));
        db.registerUser(req.getParameter("username"), req.getParameter("password"),
                req.getParameter("firstname"), req.getParameter("lastname"));
        resp.getWriter().print("");
    }
}
