package application.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import application.model.Project;
import application.model.Task;
import application.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "classpath:database/tasks/remove_all_tasks_from_tasks_table.sql",
        "classpath:database/tasks/add_first_task_to_task_table.sql"
        }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("""
            Verify create() method
            """)
public class TaskRepositoryTest {
    private static final String TASK1_NAME = "Task 1";
    private static final String TASK1_DESCRIPTION = "Description 1";
    private static final Integer TASK1_DUE_DATE = 6;
    private static final long FIRST_PROJECT_ID = 1L;
    private static final long FIRST_USER_ID = 1L;
    private static final long SECOND_PROJECT_ID = 2L;
    private static final long SECOND_USER_ID = 2L;
    private static final long INVALID_ID = 20L;
    private static Task task1;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeAll
    public static void beforeAll() {
        task1 = new Task().setId(FIRST_USER_ID)
        .setName(TASK1_NAME)
        .setDescription(TASK1_DESCRIPTION)
        .setPriority(Task.Priority.HIGH)
        .setStatus(Task.Status.IN_PROGRESS)
        .setDueDate(LocalDate.now().plusDays(TASK1_DUE_DATE))
        .setProject(new Project().setId(SECOND_PROJECT_ID))
        .setAssignee(new User().setId(SECOND_USER_ID));
    }

    @Test
    @DisplayName("""
            Verify getAllByProjectId() method with correct projectId
            """)
    public void getAllByProjectId_ValidProjectId_ReturnOneTask() {
        List<Task> expected = List.of(task1);
        List<Task> actual = taskRepository.getAllByProjectId(FIRST_PROJECT_ID);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("""
            Verify getAllByProjectId() method with invalid projectId
            """)
    public void getAllByProjectId_InvalidProjectId_ReturnEmptyList() {
        List<Task> expected = new ArrayList<>();
        List<Task> actual = taskRepository.getAllByProjectId(INVALID_ID);
        assertEquals(expected, actual);
    }
}
