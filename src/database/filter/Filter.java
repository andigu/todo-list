package database.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Andi Gu
 */
public abstract class Filter<T> {
    protected final Map<FilterType, String> filters;

    public Filter(Map<FilterType, String> filters) {
        this.filters = filters;
    }

    public Filter() {
        filters = new HashMap<>();
    }

    public void addConstraint(FilterType filterType, String value) {
        filters.put(filterType, value);
    }

    public abstract Set<T> doFilter(Set<T> objects) throws IOException;
}
