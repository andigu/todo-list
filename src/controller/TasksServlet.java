package controller;

import controller.json.JsonConstants;
import controller.json.SupportedTypeReference;
import database.Filter;
import model.User;
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
        Filter filter = converter.cast(request.getParameter(JsonConstants.FILTERS), SupportedTypeReference.Filter);
        if (user != null) {
            responseEntity.setData(db.getTasks(user, filter));
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData) throws IOException {
        return super.processPostResponse(request, response, requestData);
//        ResponseEntity<Task> responseEntity = new ResponseEntity<>();
//        String taskJson = converter.requestData.get(JsonConstants.TASK);
//        Task task = null;
//        switch (request.getParameter(JsonConstants.TASK_TYPE)) {
//            case JsonConstants.INDIVIDUAL_TASK:
//                task = converter.fromJson(taskJson, SupportedTypeReference.IndividualTask);
//                break;
//            case JsonConstants.GROUP_TASK:
//                task = converter.fromJson(taskJson, SupportedTypeReference.GroupTask);
//                break;
//            case JsonConstants.PROJECT_TASK:
//                task = converter.fromJson(taskJson, SupportedTypeReference.ProjectTask);
//                break;
//        }
//        db.insertTask(task);
//        responseEntity.setData(task);

    }
}
