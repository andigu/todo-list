package database.filter;

import model.User;
import model.group.Group;

import java.io.IOException;
import java.util.HashSet;
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
                case notJoined: result = notJoined(result, entry.getValue()); break;
                case joined: result = joined(result, entry.getValue()); break;
                case id: result = id(result, entry.getValue()); break;
            }
        }
        return result;
    }

    private Set<Group> joined(Set<Group> groups, String userId) {
        System.out.println("joinsize"+groups.size());
        for(Group group: groups) {
            for(User member: group.getMembers()) {
                System.out.println(member);
            }
        }
        System.out.println("userId "+ userId);
        return groups.stream().filter(group -> group.getMembers().stream().anyMatch(user -> user.getId().matches(userId))).collect(Collectors.toSet());

    }

    private Set<Group> notJoined(Set<Group> groups, String userId) {
        return  groups.stream().filter(group -> group.getMembers().stream().noneMatch(user -> user.getId().matches(userId))).collect(Collectors.toSet());
    }

    private Set<Group> id(Set<Group> groups, String groupId) {
        return groups.stream().filter(group -> group.getId().matches(groupId)).collect(Collectors.toSet());
    }
}
