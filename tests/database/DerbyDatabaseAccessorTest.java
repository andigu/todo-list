package database;

import model.User;
import model.group.Group;
import org.junit.Test;

/**
 * @author Andi Gu
 */
public class DerbyDatabaseAccessorTest {
    private DatabaseAccessor accessor = DerbyDatabaseAccessor.getInstance();

    @Test
    public void getUserGroups() throws Exception {
        for (Group group : accessor.getGroups(new User(1, "test", "a b"))) {
            System.out.println(group.getName());
        }
    }
}