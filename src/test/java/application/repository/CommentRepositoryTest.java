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
        Role role = new Role();
        User user = new User()
                .setId(3L)
                .setUserName("Lev")
                .setPassword("$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG")
                .setEmail("lev@gmail.com")
                .setFirstName("Lev")
                .setLastName("Borman")
                .setRoleSet(Set.of(role.setRoleName(Role.RoleName.USER)))
                .setDeleted(false);
        userRepository.save(user);
        LocalDate currentDate = LocalDate.now();
        Project project = new Project()
                .setId(3L)
                .setName("Super project!")
                .setUser(user)
                .setDescription("Current flow")
                .setStartDate(currentDate.minusMonths(6L))
                .setEndDate(currentDate.plusYears(1L))
                .setStatus(Project.Status.IN_PROGRESS)
                .setDeleted(false);
        projectRepository.save(project);
        Task task = new Task()
                .setId(2L)
                .setName("Main task")
                .setDescription("To implement user comment tests")
                .setPriority(Task.Priority.HIGH)
                .setStatus(Task.Status.NOT_STARTED)
                .setDueDate(currentDate.plusDays(5L))
                .setProject(project)
                .setAssignee(user)
                .setDeleted(false);
        taskRepository.save(task);
        LocalDateTime timeStamp = LocalDateTime.now()
                .withHour(16).withMinute(0).withSecond(0).withNano(0);
        Comment comment1 = new Comment()
                .setId(1L)
                .setTask(task)
                .setUser(user)
                .setText("To do tests to Control layer")
                .setTimeStamp(timeStamp)
                .setDeleted(false);
        Comment comment2 = new Comment()
                .setId(2L)
                .setTask(task)
                .setUser(user)
                .setText("To do tests to Repository layer")
                .setTimeStamp(timeStamp)
                .setDeleted(false);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        List<Comment> actual = commentRepository
                .findAllByTaskIdAndUserId(task.getId(), user.getId());
        for (Comment comment : actual) {
            comment.setTimeStamp(
                    comment.getTimeStamp().withHour(16).withMinute(0)
                            .withSecond(0).withNano(0));
        }
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(List.of(comment1, comment2), actual);
    }
}
