package controller.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * @author Andi Gu
 */

public class JsonConverter {
    private static JsonConverter instance = new JsonConverter();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonConverter getInstance() {
        return instance;
    }

    private JsonConverter() {
    }

    public String toJson(Object object) throws JsonProcessingException {
        return object == null ? null : mapper.writeValueAsString(object);
    }

    public <T> T toObject(String json, Class<T> tClass) throws IOException {
        return mapper.readValue(json, tClass);
    }

    public String[] toStringArray(String jsonArray) throws  IOException {
        jsonArray = jsonArray.replace("[", "").replace("]", "");
        return jsonArray.split(",");
    }

    public String toStatus(String name, Object status) {
        name = "\"" + name + "\"";
        return "{" + name + ":" + status.toString() + "}";
    }
}
