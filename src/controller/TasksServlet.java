package controller;

import controller.json.JsonConstants;
import controller.json.SupportedTypeReference;
import database.Filter;
import model.User;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;
import model.task.Task;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Andi Gu
 */

@WebServlet("/tasks")
public class TasksServlet extends ApplicationServlet {
    @Override
    public ResponseEntity<?> processGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseEntity<Set<Task>> responseEntity = new ResponseEntity<>();
        User user = getLoggedUser(request);
        Filter filter = converter.cast(request.getParameter(JsonConstants.FILTERS), SupportedTypeReference.FILTER);
        if (user != null) {
            responseEntity.setData(db.getTasks(user, filter));
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData) throws IOException {
        ResponseEntity<Task> responseEntity = new ResponseEntity<>();
        Map<String, Object> taskJson = converter.cast(requestData.get(JsonConstants.TASK), SupportedTypeReference.OBJECT_MAP);
        Task task = getTask(getLoggedUser(request), taskJson, requestData.get(JsonConstants.TASK_TYPE).toString());
        db.insertTask(task);
        responseEntity.setData(task);
        System.out.println(task.getDueDate().getTime());
        return responseEntity;
    }

    private Task getTask(User loggedUser, Map<String, Object> taskJson, String taskType) {
        switch (taskType) {
            case JsonConstants.INDIVIDUAL_TASK:
                IndividualTask individualTask = converter.cast(taskJson, IndividualTask.class);
                individualTask.setOwner(loggedUser);
                return individualTask;
            case JsonConstants.GROUP_TASK:
                GroupTask groupTask = converter.cast(taskJson, SupportedTypeReference.GROUP_TASK);
                groupTask.setGroup(db.getGroupById(taskJson.get(JsonConstants.GROUP_ID).toString()));
                return groupTask;
            case JsonConstants.PROJECT_TASK:
                ProjectTask projectTask = converter.cast(taskJson, SupportedTypeReference.PROJECT_TASK);
                projectTask.setProject(db.getProjectById(taskJson.get(JsonConstants.PROJECT_ID).toString()));
                return projectTask;
        }
        return null;
    }
}
