package controller;

import controller.json.JsonConverter;
import database.DatabaseAccessor;
import database.DerbyDatabaseAccessor;

import javax.servlet.http.HttpServlet;

/**
 * @author Andi Gu
 */
public abstract class ApplicationServlet extends HttpServlet {
    DatabaseAccessor db = DerbyDatabaseAccessor.getInstance();
    JsonConverter converter = JsonConverter.getInstance();
}
