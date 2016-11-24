package database;

import model.Identifiable;
import model.User;
import model.group.Group;
import model.group.Project;
import model.task.IndividualTask;
import model.task.ProjectTask;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;

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
        ProjectTask[] tasks = accessor.getAllProjectTasks(accessor.getUserById(userId)).toArray(new ProjectTask[] {});
        Arrays.sort(tasks);
        Assert.assertEquals(new Long(1), tasks[0].getId());
        Assert.assertEquals(new Long(2), tasks[1].getId());
        Assert.assertEquals(2, tasks.length);
    }

    @Test
    public void getGroups() throws Exception {

    }

    @Test
    public void getProjects() throws Exception {
        Project[] projects = accessor.getProjects(accessor.getUserById(userId)).toArray(new Project[] {});
        Arrays.sort(projects);
        Assert.assertEquals(new Long(1), projects[0].getId());
        Assert.assertEquals(new Long(2), projects[1].getId());
        Assert.assertEquals(2, projects.length);
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