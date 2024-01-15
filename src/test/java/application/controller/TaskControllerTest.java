package application.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.task.TaskRequestDto;
import application.dto.task.TaskResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@Sql(scripts = "classpath:database/tasks/remove_all_tasks_from_tasks_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {
    protected static MockMvc mockMvc;

    private static final String EXCLUDE_FIELD_DUE_DATE = "dueDate";
    private static final String EXCLUDE_FIELD_ID = "id";
    private static final String PARAMETER_URL_PROJECT_ID_1 = "/projectId" + "/1";
    private static final String PARAMETER_URL_TASK_ID_1 = "/1";
    private static final String PARAMETER_URL_TASK_ID_4 = "/4";
    private static final String PARAMETER_URL_TASK_ID_5 = "/5";
    private static final String PRIORITY_HIGH = "HIGH";
    private static final String STATUS_IN_COMPLETED = "COMPLETED";
    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private static final String TASK1_NAME = "Task 1";
    private static final Integer FIRST_TASK = 0;
    private static final String TASK1_DESCRIPTION = "Description for 1";
    private static final String TASK2_NAME = "Task 2";
    private static final String TASK2_DESCRIPTION = "Description 2";

    private static final String UPDATED_TASK3_NAME = "Updated task 3";
    private static final String UPDATED_TASK3_DESCRIPTION = "Updated description 3";
    private static final Integer UPDATED_TASK3_DUE_DATE = 6;
    private static final String TASK3_NAME = "Task 3";
    private static final String TASK3_DESCRIPTION = "Description for 3";
    private static final Integer TASK3_DUE_DATE = 9;
    private static final long FIRST_PROJECT_ID = 1L;
    private static final long FIRST_TASK_ID = 1L;
    private static final long FIRST_USER_ID = 1L;
    private static final long SECOND_TASK_ID = 2L;
    private static final long SECOND_PROJECT_ID = 2L;
    private static final long SECOND_USER_ID = 2L;
    private static final long UPDATED_THIRD_TASK_ID = 4L;
    private static final String URL = "/api/tasks";
    private static final String USER_ROLE = "ADMIN";
    private static final String ADMIN = "admin";
    private static List<TaskResponseDto> taskResponseDtos;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        TaskResponseDto taskResponseDto1 = new TaskResponseDto()
                .setId(FIRST_TASK_ID)
                .setName(TASK1_NAME)
                .setDescription(TASK1_DESCRIPTION)
                .setPriority(PRIORITY_HIGH)
                .setStatus(STATUS_IN_PROGRESS)
                .setProjectId(FIRST_PROJECT_ID)
                .setAssigneeId(FIRST_USER_ID);

        TaskResponseDto taskResponseDto2 = new TaskResponseDto()
                .setId(SECOND_TASK_ID)
                .setName(TASK2_NAME)
                .setDescription(TASK2_DESCRIPTION)
                .setPriority(PRIORITY_HIGH)
                .setStatus(STATUS_IN_PROGRESS)
                .setProjectId(SECOND_PROJECT_ID)
                .setAssigneeId(SECOND_USER_ID);

        taskResponseDtos = List.of(taskResponseDto1, taskResponseDto2);
    }

    @Test
    @DisplayName("""
            Verify create() method
            """)
    @Sql(scripts = "classpath:database/tasks/remove_one_task_from_tasks_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = ADMIN, roles = USER_ROLE)
    void create_ValidRequestDto_ReturnResponseDto() throws Exception {
        // given
        TaskRequestDto taskRequestDto = new TaskRequestDto()
                .setName(TASK3_NAME)
                .setDescription(TASK3_DESCRIPTION)
                .setPriority(PRIORITY_HIGH)
                .setStatus(STATUS_IN_PROGRESS)
                .setDueDate(LocalDate.now().plusDays(TASK3_DUE_DATE))
                .setProjectId(FIRST_PROJECT_ID)
                .setAssigneeId(FIRST_USER_ID);
        String jsonBody = objectMapper.writeValueAsString(taskRequestDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)).andExpect(status().isCreated()).andReturn();

        // then
        TaskResponseDto expected = new TaskResponseDto()
                .setName(taskRequestDto.getName())
                .setDescription(taskRequestDto.getDescription())
                .setPriority(taskRequestDto.getPriority())
                .setStatus(taskRequestDto.getStatus())
                .setDueDate(taskRequestDto.getDueDate())
                .setProjectId(taskRequestDto.getProjectId())
                .setAssigneeId(taskRequestDto.getAssigneeId());
        TaskResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(),
                TaskResponseDto.class);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, EXCLUDE_FIELD_ID);
    }

    @Test
    @DisplayName("""
            Verify getAll() method
            """)

    @WithMockUser(username = ADMIN, roles = USER_ROLE)
    void getAll_NonEmptyDb_ReturnExpectedTaskDtoList() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get(URL + PARAMETER_URL_PROJECT_ID_1)).andReturn();

        // then
        List<TaskResponseDto> expected = taskResponseDtos;
        List<TaskResponseDto> actual = objectMapper.readValue(mvcResult
                        .getResponse().getContentAsString(),
                new TypeReference<List<TaskResponseDto>>() {});
        Assertions.assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected, actual, EXCLUDE_FIELD_ID);
    }

    @WithMockUser(username = ADMIN, roles = USER_ROLE)
    @DisplayName("""
            Verify getById() method
            """)
    @Test
    void getTaskById_ValidTaskId_ReturnTaskResponseDto() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get(URL + PARAMETER_URL_TASK_ID_1))
                .andReturn();

        // then
        TaskResponseDto expected = taskResponseDtos.get(FIRST_TASK);
        TaskResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        TaskResponseDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected,actual,EXCLUDE_FIELD_DUE_DATE);
    }

    @Test
    @Sql(scripts = "classpath:database/tasks/add_fourth_task_to_tasks_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/tasks/remove_updated_fourth_task_from_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Verify update() method
            """)
    @WithMockUser(username = ADMIN, roles = USER_ROLE)
    void update_ValidRequestDto_ReturnResponseDto() throws Exception {
        // given
        TaskRequestDto taskRequestDto3 = new TaskRequestDto()
                .setName(UPDATED_TASK3_NAME)
                .setDescription(UPDATED_TASK3_DESCRIPTION)
                .setPriority(PRIORITY_HIGH)
                .setStatus(STATUS_IN_COMPLETED)
                .setDueDate(LocalDate.now().plusDays(UPDATED_TASK3_DUE_DATE))
                .setProjectId(SECOND_PROJECT_ID)
                .setAssigneeId(SECOND_USER_ID);
        String jsonBody = objectMapper.writeValueAsString(taskRequestDto3);

        // when
        MvcResult mvcResult = mockMvc.perform(put(URL + PARAMETER_URL_TASK_ID_4).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andReturn();

        // then
        TaskResponseDto expected = new TaskResponseDto()
                .setId(UPDATED_THIRD_TASK_ID)
                .setName(UPDATED_TASK3_NAME)
                .setDescription(UPDATED_TASK3_DESCRIPTION)
                .setPriority(PRIORITY_HIGH)
                .setStatus(STATUS_IN_COMPLETED)
                .setDueDate(LocalDate.now().plusDays(UPDATED_TASK3_DUE_DATE))
                .setProjectId(SECOND_PROJECT_ID)
                .setAssigneeId(SECOND_USER_ID);
        TaskResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(), TaskResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"classpath:database/tasks/add_fifth_task_to_task_table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("""
            Verify delete() method
            """)
    @WithMockUser(username = ADMIN, roles = USER_ROLE)
    void delete_ValidInputId_Success() throws Exception {
        // when
        mockMvc.perform(delete(URL + PARAMETER_URL_TASK_ID_5))
                .andExpect(status().isNoContent());
        MvcResult mvcResult = mockMvc.perform(get(URL + PARAMETER_URL_PROJECT_ID_1)).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

        // then
        List<TaskResponseDto> expected = taskResponseDtos;
        List<TaskResponseDto> actual = objectMapper.readValue(mvcResult
                        .getResponse().getContentAsString(),
                new TypeReference<List<TaskResponseDto>>() {});

        assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected, actual, EXCLUDE_FIELD_ID);
    }
}
