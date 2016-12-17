package controller.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Andi Gu
 */

public class StateConverter {
    private static StateConverter instance = new StateConverter();
    private final ObjectMapper mapper;

    public static StateConverter getInstance() {
        return instance;
    }

    private StateConverter() {
        mapper = new ObjectMapper();
    }

    public String toJson(Object object) throws JsonProcessingException {
        return object == null ? null : mapper.writeValueAsString(object);
    }

    public <T> T fromJson(String json, SupportedTypeReference typeReference) throws IOException {
        return json == null ? null: mapper.readValue(json, typeReference.getTypeReference());
    }


    public <T> T cast(Object object, SupportedTypeReference typeReference) {
        return object == null ? null : mapper.convertValue(object, typeReference.getTypeReference());
    }
}
