package controller;

import controller.json.JsonConstant;
import model.User;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;
import model.task.Task;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Andi Gu
 */

@WebServlet("/tasks")
public class TasksServlet extends ApplicationServlet {
    @Override
    public void writeGetResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws IOException {
        User user = getLoggedUser(request);
        if (user != null) {
            String[] taskTypes;
            if (!hasParameter(request, JsonConstant.TASK_TYPES)) {
                taskTypes = new String[]{JsonConstant.INDIVIDUAL_TASK, JsonConstant.GROUP_TASK, JsonConstant.PROJECT_TASK};
            } else {
                taskTypes = converter.fromJson(request.getParameter(JsonConstant.TASK_TYPES), String[].class);
            }
            Map<String, Set<? extends Task>> tasks = new HashMap<>();
            Set<? extends Task> holder = new HashSet<>();
            for (String taskType : taskTypes) {
                switch (taskType) {
                    case JsonConstant.INDIVIDUAL_TASK:
                        holder = db.getAllIndividualTasks(user);
                        break;
                    case JsonConstant.GROUP_TASK:
                        holder = db.getAllGroupTasks(user);
                        break;
                    case JsonConstant.PROJECT_TASK:
                        holder = db.getAllProjectTasks(user);
                        break;
                }
                if (holder.size() > 0) {
                    tasks.put(taskType, holder);
                }
            }
            jsonMap.put(JsonConstant.TASKS, tasks);
        }
    }

    @Override
    public void writePostResponse(HttpServletRequest request, Map<String, Object> jsonMap) throws IOException {
        String taskJson = request.getParameter(JsonConstant.TASK);
        Task task = null;
        switch (request.getParameter(JsonConstant.TASK_TYPE)) {
            case JsonConstant.INDIVIDUAL_TASK:
                task = converter.fromJson(taskJson, IndividualTask.class);
                break;
            case JsonConstant.GROUP_TASK:
                task = converter.fromJson(taskJson, GroupTask.class);
                break;
            case JsonConstant.PROJECT_TASK:
                task = converter.fromJson(taskJson, ProjectTask.class);
                break;
        }
        db.insertTask(task);
        jsonMap.put(JsonConstant.TASK, task);
    }
}
