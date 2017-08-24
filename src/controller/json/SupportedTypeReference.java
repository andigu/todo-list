package controller.json;

import com.fasterxml.jackson.core.type.TypeReference;
import database.filter.TaskFilter;
import model.Session;
import model.group.Topic;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;

import java.util.Map;

/**
 * @author Andi Gu
 */
public enum SupportedTypeReference {
    OBJECT_MAP(new TypeReference<Map<String, Object>>() {
    }),
    STRING_MAP(new TypeReference<Map<String, Object>>() {
    }),
    BOOLEAN(new TypeReference<Boolean>() {
    }),
    FILTER(new TypeReference<TaskFilter>() {
    }),
    INDIVIDUAL_TASK(new TypeReference<IndividualTask>() {
    }),
    PROJECT_TASK(new TypeReference<ProjectTask>() {
    }),
    GROUP_TASK(new TypeReference<GroupTask>() {
    }),
    STRING_ARRAY(new TypeReference<String[]>() {
    }),
    SESSION(new TypeReference<Session>(){
    }),
    TOPIC(new TypeReference<Topic>() {
    });

    private TypeReference<?> typeReference;

    SupportedTypeReference(TypeReference<?> typeReference) {
        this.typeReference = typeReference;
    }

    public TypeReference<?> getTypeReference() {
        return typeReference;
    }
}
