package database.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import controller.json.JsonConstants;

/**
 * @author Andi Gu
 */

@JsonFormat
public enum FilterType {
    taskTypes(JsonConstants.TASK_TYPES_FILTER),
    taskId(JsonConstants.TASK_ID_FILTER),
    parentId(JsonConstants.PARENT_ID_FILTER),
    startDate(JsonConstants.START_DATE),
    endDate(JsonConstants.END_DATE),
    notJoined(JsonConstants.NOT_JOINED_FILTER),
    joined(JsonConstants.JOINED_FILTER);


    private String value;
    FilterType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static FilterType fromValue(String value) {
        return FilterType.valueOf(value);
    }
}
