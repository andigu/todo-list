package controller.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Andi Gu
 */

public class JsonConverter {
    private static JsonConverter instance = new JsonConverter();
    private final ObjectMapper mapper;

    public static JsonConverter getInstance() {
        return instance;
    }

    private JsonConverter() {
        mapper = new ObjectMapper();
    }

    public String toJson(Object object) throws JsonProcessingException {
        return object == null ? null : mapper.writeValueAsString(object);
    }

    public <T> T fromJson(String json, Class<T> tClass) throws IOException {
        if (json == null) {
            return null;
        }
        else {
            return mapper.readValue(json, tClass);
        }
    }
}
