package application.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import application.model.Attachment;
import application.model.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@Sql(scripts = "classpath:database/projects/add_one_project_to_projects_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/tasks/add_one_task_from_tasks_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/attachments/add_three_attachments_to_attachments_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/attachments/remove_three_attachments_from_attachments_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:database/tasks/remove_one_task.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:database/projects/remove_one_project.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AttachmentRepositoryTest {
    private static List<Attachment> attachmentList;
    private static final Long VALID_TASK_ID = 6L;
    private static final Long INVALID_TASK_ID = 200L;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @BeforeAll
    static void beforeAll() {
        Attachment firstAttachment = new Attachment()
                .setId(1L).setTask(new Task().setId(6L)).setFileName("test1.txt")
                .setDropBoxFileId("sdsdgsgsdggsfgsd");
        Attachment secondAttachment = new Attachment()
                .setId(2L).setTask(new Task().setId(6L)).setFileName("test2.txt")
                .setDropBoxFileId("sdsdgsgsdggsfgsdtg");
        Attachment thirdAttachment = new Attachment()
                .setId(3L).setTask(new Task().setId(6L)).setFileName("test3.txt")
                .setDropBoxFileId("sddkfgksdjgkdkghd");
        attachmentList = List.of(firstAttachment, secondAttachment, thirdAttachment);
    }

    @Test
    @DisplayName("""
            Verify findAllByTaskId() method
            """)
    void findAllByTaskId_ValidTaskId_ReturnExpectedList() {
        List<Attachment> actual = attachmentRepository.findAllByTaskId(VALID_TASK_ID);
        assertEquals(3, actual.size());
        EqualsBuilder.reflectionEquals(attachmentList, actual, "uploadDate");
    }

    @Test
    @DisplayName("""
            Verify findAllByTaskId() method
            """)
    void findAllByTaskId_InvalidTaskId_ReturnEmptyList() {
        List<Attachment> actual = attachmentRepository.findAllByTaskId(INVALID_TASK_ID);
        assertEquals(0, actual.size());
    }
}
