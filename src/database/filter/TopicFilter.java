package database.filter;

import model.group.Topic;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Susheel Kona
 */
public class TopicFilter extends Filter<Topic> {

    @Override
    public Set<Topic> doFilter(Set<Topic> objects) throws IOException {
        for(Map.Entry<FilterType, String> filter: this.filters.entrySet()) {
            switch (filter.getKey()) {
                case id: objects = id(objects, filter.getValue().toString()); break;
                case parentId: objects = parentId(objects, filter.getValue().toString()); break;
            }
        }
        return objects;
    }

    private Set<Topic> id(Set<Topic> results, String id) {
        return results.stream().filter(topic -> topic.getId().matches(id)).collect(Collectors.toSet());
    }

    private Set<Topic> parentId(Set<Topic> results, String parentId) {
        return results.stream().filter(topic -> topic.getGroupId().matches(parentId)).collect(Collectors.toSet());
    }
}
