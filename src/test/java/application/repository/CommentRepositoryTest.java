package application.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.model.Comment;
import application.model.Project;
import application.model.Role;
import application.model.Task;
import application.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {
    private static final Long COMMENT1_ID = 1L;
    private static final Long COMMENT2_ID = 2L;
    private static final Long PROJECT_ID = 3L;
    private static final Long TASK_ID = 2L;
    private static final Long USER_ID = 3L;
    private static final String COMMENT1_TEXT = "To do tests to Control layer";
    private static final String COMMENT2_TEXT = "To do tests to Repository layer";
    private static final String PROJECT_DESCRIPTION = "Current flow";
    private static final String PROJECT_NAME = "Super project!";
    private static final String TASK_DESCRIPTION = "To implement user comment tests";
    private static final String TASK_NAME = "Main task";
    private static final String USER_EMAIL = "john@gmail.com";
    private static final String USER_FIRST_NAME = "John";
    private static final String USER_NAME = "John";
    private static final String USER_LAST_NAME = "Lollipop";
    private static final String USER_PASSWORD =
            "$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG";

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Get all user comments to task")
    public void findAllByTaskIdAndUserId_WithUser_ShouldReturnList() {
        Role role1 = new Role();
        role1.setRoleName(Role.RoleName.ADMIN);
        Role role2 = new Role();
        role2.setRoleName(Role.RoleName.USER);
        roleRepository.save(role1);
        roleRepository.save(role2);
        User user = new User()
                .setId(USER_ID)
                .setUserName(USER_NAME)
                .setPassword(USER_PASSWORD)
                .setEmail(USER_EMAIL)
                .setFirstName(USER_FIRST_NAME)
                .setLastName(USER_LAST_NAME)
                .setRoleSet(Set.of(role1))
                .setDeleted(false);
        userRepository.save(user);
        LocalDate currentDate = LocalDate.now();
        Project project = new Project()
                .setId(PROJECT_ID)
                .setName(PROJECT_NAME)
                .setUser(user)
                .setDescription(PROJECT_DESCRIPTION)
                .setStartDate(currentDate.minusMonths(6L))
                .setEndDate(currentDate.plusYears(1L))
                .setStatus(Project.Status.IN_PROGRESS)
                .setDeleted(false);
        projectRepository.save(project);
        Task task = new Task()
                .setId(TASK_ID)
                .setName(TASK_NAME)
                .setDescription(TASK_DESCRIPTION)
                .setPriority(Task.Priority.HIGH)
                .setStatus(Task.Status.NOT_STARTED)
                .setDueDate(currentDate.plusDays(5L))
                .setProject(project)
                .setAssignee(user)
                .setDeleted(false);
        taskRepository.save(task);
        LocalDateTime timeStamp = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        Comment comment1 = new Comment()
                .setId(COMMENT1_ID)
                .setTask(task)
                .setUser(user)
                .setText(COMMENT1_TEXT)
                .setTimeStamp(timeStamp)
                .setDeleted(false);
        Comment comment2 = new Comment()
                .setId(COMMENT2_ID)
                .setTask(task)
                .setUser(user)
                .setText(COMMENT2_TEXT)
                .setTimeStamp(timeStamp)
                .setDeleted(false);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        List<Comment> actual = commentRepository
                .findAllByTaskIdAndUserId(task.getId(), user.getId());
        for (Comment comment : actual) {
            comment.setTimeStamp(
                    comment.getTimeStamp().withHour(0).withMinute(0)
                            .withSecond(0).withNano(0));
        }
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(List.of(comment1, comment2), actual);
    }
}
