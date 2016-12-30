package database.filter;

import model.group.Group;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Andi Gu
 */
public class GroupFilter extends Filter<Group> {
    @Override
    public Set<Group> doFilter(Set<Group> objects) throws IOException {
        Set<Group> result = objects;
        for (Map.Entry<FilterType, String> entry : filters.entrySet()) {
            switch (entry.getKey()) {
                case notJoined: result = notJoined(result, entry.getValue());
            }
        }
        return result;
    }

    private Set<Group> notJoined(Set<Group> groups, String userId) {
        return groups.stream().filter(group -> group.getMembers().stream().anyMatch(user -> user.getId().equals(userId))).collect(Collectors.toSet());
    }
}
