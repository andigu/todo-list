package controller.json;

import com.fasterxml.jackson.core.type.TypeReference;
import database.Filter;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;

import java.util.Map;

/**
 * @author Andi Gu
 */
public enum SupportedTypeReference {
    ObjectMap(new TypeReference<Map<String, Object>>() {
    }),
    StringMap(new TypeReference<Map<String, Object>>() {
    }),
    Boolean(new TypeReference<Boolean>() {
    }),
    Filter(new TypeReference<Filter>() {
    }),
    IndividualTask(new TypeReference<IndividualTask>() {
    }),
    ProjectTask(new TypeReference<ProjectTask>() {
    }),
    GroupTask(new TypeReference<GroupTask>() {
    }),
    StringArray(new TypeReference<String[]>() {
    });

    private TypeReference<?> typeReference;

    SupportedTypeReference(TypeReference<?> typeReference) {
        this.typeReference = typeReference;
    }

    public TypeReference<?> getTypeReference() {
        return typeReference;
    }
}
