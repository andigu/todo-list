package database;

import model.User;
import model.group.Group;
import model.group.Project;
import model.task.IndividualTask;
import model.task.ProjectTask;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * @author Andi Gu
 */
public class DerbyDatabaseAccessorTest {
    private DatabaseAccessor accessor = DerbyDatabaseAccessor.getInstance();
    private Long userId;

    @Before
    public void setUp() {
        userId = 1L;
    }

    @Test
    public void getUserById() throws Exception {
        User user = accessor.getUserById(userId);
        Assert.assertEquals(userId, user.getId());
    }

    @Test
    public void getAllIndividualTasks() throws Exception {

    }

    @Test
    public void getAllGroupTasks() throws Exception {

    }

    @Test
    public void getAllProjectTasks() throws Exception {
//        Set<ProjectTask> tasks = accessor.getAllProjectTasks(accessor.getUserById(userId));
//        for (ProjectTask task : tasks) {
//            Assert.assertEquals(userId);
//        }
    }

    @Test
    public void getGroups() throws Exception {

    }

    @Test
    public void getProjects() throws Exception {

    }

    @Test
    public void getGroupTasks() throws Exception {

    }

    @Test
    public void getProjectTasks() throws Exception {

    }

    @Test
    public void getUsersCompletedGroupTask() throws Exception {

    }
}