package controller;

import model.User;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Andi Gu
 */

@WebServlet("/tasks")
public class TasksServlet extends ApplicationServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getLoggedUser(req);
        if (user != null) {
            String[] taskTypes;
            if (!hasParameter(req, "task-types")) {
                taskTypes = new String[]{"individual", "group", "project"};
            } else {
                taskTypes = converter.toStringArray(req.getParameter("task-types"));
            }
            Map<String, Set<? extends Task>> tasks = new HashMap<>();
            for (String taskType : taskTypes) {
                Set<? extends Task> holder = new HashSet<>();
                switch (taskType) {
                    case "individual":
                        holder = db.getAllIndividualTasks(user);
                        break;
                    case "group":
                        holder = db.getAllGroupTasks(user);
                        break;
                    case "project":
                        holder = db.getAllProjectTasks(user);
                        break;
                }
                if (holder.size() > 0) {
                    tasks.put(taskType, holder);
                }
            }
            resp.setContentType("json/application");
            resp.getWriter().write(converter.toJson(tasks));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
