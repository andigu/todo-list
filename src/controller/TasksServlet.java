package controller;

import controller.json.JsonConstants;
import database.Filter;
import model.User;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;
import model.task.Task;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author Andi Gu
 */

@WebServlet("/tasks")
public class TasksServlet extends ApplicationServlet {
    @Override
    public void writeGetResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws IOException {
        User user = getLoggedUser(request);
        Filter filter = converter.fromJson(request.getParameter(JsonConstants.FILTERS), Filter.class);

        if (user != null) {
            jsonMap.put(JsonConstants.TASKS, db.getTasks(user, filter));
        }
    }

    @Override
    public void writePostResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws IOException {
        String taskJson = request.getParameter(JsonConstants.TASK);
        Task task = null;
        switch (taskJson) {
            case JsonConstants.INDIVIDUAL_TASK:
                task = converter.fromJson(taskJson, IndividualTask.class);
                break;
            case JsonConstants.GROUP_TASK:
                task = converter.fromJson(taskJson, GroupTask.class);
                break;
            case JsonConstants.PROJECT_TASK:
                task = converter.fromJson(taskJson, ProjectTask.class);
                break;
        }
        db.insertTask(task);
        jsonMap.put(JsonConstants.TASK, task);
    }
}
