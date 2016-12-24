package controller.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;

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
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    public String toJson(Object object) throws JsonProcessingException {
        return object == null ? null : mapper.writeValueAsString(object);
    }

    public <T> T fromJson(String json, SupportedTypeReference typeReference) throws IOException {
        return json == null ? null: mapper.readValue(json, typeReference.getTypeReference());
    }

    public <T> T cast(Object object, SupportedTypeReference typeReference) {
        if (object != null) {
            System.out.println(mapper.convertValue(object, typeReference.getTypeReference()).toString());
        }
        return object == null ? null : mapper.convertValue(object, typeReference.getTypeReference());
    }

    public <T> T cast(Object object, Class<T> tClass) {
        return object == null ? null : mapper.convertValue(object, tClass);
    }
}
