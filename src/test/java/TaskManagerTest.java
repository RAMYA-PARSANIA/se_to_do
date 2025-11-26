import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

/**
 * Unit tests for Task Manager application
 */
public class TaskManagerTest {
    private TaskManager manager;
    private final String testFile = "test_tasks.json";

    @BeforeEach
    public void setUp() {
        manager = new TaskManager(testFile);
    }

    @AfterEach
    public void tearDown() {
        File file = new File(testFile);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testCreateTask() {
        boolean result = manager.createTask("Test task");
        assertTrue(result);
        String output = manager.displayAllTasks();
        assertTrue(output.contains("Test task"));
    }

    @Test
    public void testCreateEmptyTask() {
        boolean result = manager.createTask("");
        assertFalse(result);
    }

    @Test
    public void testCreateNullTask() {
        boolean result = manager.createTask(null);
        assertFalse(result);
    }

    @Test
    public void testMarkAsDone() {
        manager.createTask("Test task");
        boolean result = manager.markAsDone(1);
        assertTrue(result);
        String output = manager.displayAllTasks();
        assertTrue(output.contains("✔"));
    }

    @Test
    public void testMarkAsDoneInvalidId() {
        manager.createTask("Test task");
        boolean result = manager.markAsDone(999);
        assertFalse(result);
    }

    @Test
    public void testRemoveTask() {
        manager.createTask("Test task 1");
        manager.createTask("Test task 2");
        boolean result = manager.removeTask(1);
        assertTrue(result);
        String output = manager.displayAllTasks();
        assertFalse(output.contains("Test task 1"));
        assertTrue(output.contains("Test task 2"));
    }

    @Test
    public void testRemoveTaskInvalidId() {
        manager.createTask("Test task");
        boolean result = manager.removeTask(999);
        assertFalse(result);
    }

    @Test
    public void testRemoveAllTasks() {
        manager.createTask("Test task 1");
        manager.createTask("Test task 2");
        boolean result = manager.removeAllTasks();
        assertTrue(result);
        String output = manager.displayAllTasks();
        assertTrue(output.contains("empty"));
    }

    @Test
    public void testDisplayEmptyList() {
        String output = manager.displayAllTasks();
        assertTrue(output.contains("empty"));
    }

    @Test
    public void testPersistAndRetrieve() {
        manager.createTask("Test task");
        TaskManager newManager = new TaskManager(testFile);
        String output = newManager.displayAllTasks();
        assertTrue(output.contains("Test task"));
    }
}
