package controller;

import model.User;
import model.task.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andi Gu
 */

@WebServlet("/tasks")
public class TasksServlet extends ApplicationServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = db.getUserById(Long.parseLong(req.getParameter("user-id")));
        String[] taskTypes = converter.toStringArray(req.getParameter("task-types"));
        System.out.println(Arrays.toString(taskTypes));
        Set<Task> tasks = new HashSet<>();
        for (String taskType : taskTypes) {
            switch (taskType) {
                case "individual":
                    tasks.addAll(db.getAllIndividualTasks(user));
                case "group":
                    tasks.addAll(db.getAllGroupTasks(user));
                case "project":
                    tasks.addAll(db.getAllProjectTasks(user));
            }
        }
        resp.setContentType("json/application");
        resp.getWriter().write(converter.toJson(tasks.toArray()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
