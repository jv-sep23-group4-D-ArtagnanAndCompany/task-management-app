package application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.model.Role;
import application.model.Task;
import application.model.User;
import application.service.impl.TelegramServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerTest {
    protected static MockMvc mockMvc;
    private static final Long ADMIN_ROLE_ID = 1L;
    private static final Long COMMENT1_ID = 1L;
    private static final Long COMMENT2_ID = 2L;
    private static final Long COMMENT3_ID = 3L;
    private static final Long TASK_ID = 2L;
    private static final Long USER_ROLE_ID = 2L;
    private static final Long USER_ID = 3L;
    private static final String CLASS_PATH_RESOURCE_ADD =
            "database/comments/add_two_comments.sql";
    private static final String CLASS_PATH_RESOURCE_DELETE =
            "database/comments/remove_two_added_comments.sql";
    private static final String COMMENT1_TEXT = "Some comment 1";
    private static final String COMMENT2_TEXT = "Some comment 2";
    private static final String USER_EMAIL = "john@gmail.com";
    private static final String USER_PASSWORD =
            "$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG";
    @MockBean
    private TelegramServiceImpl telegramServiceImpl;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    @SneakyThrows
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext) {
        teardown(dataSource);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            CLASS_PATH_RESOURCE_ADD)
            );
        }
    }

    @BeforeEach
    void setUp() {
        User john = new User();
        john.setId(USER_ID);
        john.setEmail(USER_EMAIL);
        john.setPassword(USER_PASSWORD);
        Role roleUser = new Role().setId(USER_ROLE_ID).setRoleName(Role.RoleName.USER);
        Role roleAdmin = new Role().setId(ADMIN_ROLE_ID).setRoleName(Role.RoleName.ADMIN);
        john.setRoleSet(Set.of(roleAdmin, roleUser));
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(john,
                john.getPassword(), john.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterAll
    private static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all user comments")
    public void getCommentsByTaskId_WithUserAndComments_ReturnList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/comments?taskId={taskId}", 2))
                .andReturn();
        User john = new User()
                .setId(USER_ID);
        CommentResponseDto commentResponseDto1 = new CommentResponseDto()
                .setId(COMMENT1_ID)
                .setTaskId(TASK_ID)
                .setUserId(john.getId())
                .setText(COMMENT1_TEXT)
                .setTimeStamp(LocalDateTime.now()
                        .withHour(0).withMinute(0).withSecond(0).withNano(0));
        CommentResponseDto commentResponseDto2 = new CommentResponseDto()
                .setId(COMMENT2_ID)
                .setTaskId(TASK_ID)
                .setUserId(john.getId())
                .setText(COMMENT2_TEXT)
                .setTimeStamp(LocalDateTime.now()
                         .withHour(0).withMinute(0).withSecond(0).withNano(0));
        List<CommentResponseDto> expected = List.of(commentResponseDto1, commentResponseDto2);
        List<CommentResponseDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        for (CommentResponseDto comment : actual) {
            comment.setTimeStamp(LocalDateTime.now()
                    .withHour(0).withMinute(0).withSecond(0).withNano(0));
        }
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Create user comment to task")
    public void createNewComment_WithUserAndComment_ReturnCommentResponseDto() throws Exception {
        Task task = new Task()
                .setId(TASK_ID);
        User john = new User()
                .setId(USER_ID);
        CommentRequestDto requestDto = new CommentRequestDto()
                .setTaskId(task.getId())
                .setText(COMMENT1_TEXT);
        String jsonBody = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isCreated()).andReturn();
        CommentResponseDto actual = new CommentResponseDto()
                .setId(COMMENT3_ID)
                .setTaskId(task.getId())
                .setUserId(john.getId())
                .setText(COMMENT1_TEXT)
                .setTimeStamp(LocalDateTime.now()
                        .withHour(0).withMinute(0).withSecond(0).withNano(0));
        CommentResponseDto expected = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        expected.setTimeStamp(LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0).withNano(0));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            CLASS_PATH_RESOURCE_DELETE)
            );
        }
    }
}
