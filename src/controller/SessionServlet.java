package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Just for front end to ping if a session for the user exists
 *
 * @author Andi Gu
 */

@WebServlet("/sessions")
public class SessionServlet extends ApplicationServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("json/application");
        switch (req.getParameter("cmd")) {
            case "ping":
                resp.getWriter().write(converter.toStatus("logged-in", getLoggedUser(req) != null));
                break;
            case "user-inf":
                if (getLoggedUser(req) != null) {
                    resp.getWriter().write(converter.toJson(getLoggedUser(req)));
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
