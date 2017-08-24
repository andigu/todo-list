package controller;

import controller.json.SupportedTypeReference;
import database.filter.TopicFilter;
import model.Session;
import model.group.Topic;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Susheel Kona
 */
@WebServlet("/topics")
public class TopicServlet extends ApplicationServlet {
    @Override
    public ResponseEntity<?> processGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseEntity<Set<Topic>> responseEntity = new ResponseEntity<>();
        responseEntity.setData(db.getTopics(addConstraints(new TopicFilter(), request)));
        return responseEntity;
    }

    @Override
    public ResponseEntity<?> processPostResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> requestData, Session session) throws IOException {
        ResponseEntity<Topic> responseEntity = new ResponseEntity<>();
        Topic topic = converter.cast(requestData.get("topic"), SupportedTypeReference.TOPIC);
        responseEntity.setData(db.insertTopic(topic));
        return responseEntity;
    }
}
