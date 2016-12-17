package database;

import controller.json.JsonConstants;
import controller.json.StateConverter;
import controller.json.SupportedTypeReference;
import model.task.GroupTask;
import model.task.IndividualTask;
import model.task.ProjectTask;
import model.task.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Andi Gu
 */
public class Filter {
    private final Map<FilterType, String> filters;
    private final Map<Class<? extends Task>, String> classJsonMap = new HashMap<Class<? extends Task>, String>() {{
        put(IndividualTask.class, JsonConstants.INDIVIDUAL_TASK);
        put(GroupTask.class, JsonConstants.GROUP_TASK);
        put(ProjectTask.class, JsonConstants.PROJECT_TASK);
    }};

    public Filter(Map<FilterType, String> filters) {
        this.filters = filters;
    }

    Set<Task> doFilter(Set<Task> tasks) throws IOException {
        Set<Task> result = new HashSet<>();
        for (Map.Entry<FilterType, String> entry : filters.entrySet()) {
            switch (entry.getKey()) {
                case parentId:
                    result = parentId(tasks, entry.getValue());
                    break;
                case taskId:
                    result = taskId(tasks, entry.getValue());
                    break;
                case taskTypes:
                    result = taskTypes(tasks, entry.getValue());
                    break;
            }
        }
        return result;
    }

    private Set<Task> parentId(Set<Task> tasks, String id) {
        return tasks.stream().filter(task -> {
            if (task instanceof IndividualTask) { // TODO SLOPPY
                return false;
            } else if (task instanceof GroupTask) {
                return ((GroupTask) task).getGroup().getId().equals(id);
            } else if (task instanceof ProjectTask) {
                return ((ProjectTask) task).getProject().getId().equals(id);
            }
            return false;
        }).collect(Collectors.toSet());
    }

    private Set<Task> taskId(Set<Task> tasks, String id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).collect(Collectors.toSet());
    }

    private Set<Task> taskTypes(Set<Task> tasks, String taskTypesArray) throws IOException {
        String[] taskTypes = StateConverter.getInstance().fromJson(taskTypesArray, SupportedTypeReference.StringArray); // TODO Sloppy
        return tasks.stream().filter(task -> Stream.of(taskTypes).anyMatch(s -> s.equals(classJsonMap.get(task.getClass())))).collect(Collectors.toSet());
    }
}
