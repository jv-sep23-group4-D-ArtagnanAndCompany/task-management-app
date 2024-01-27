package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.dto.task.TaskRequestDto;
import application.dto.task.TaskResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.TaskMapper;
import application.model.Project;
import application.model.Task;
import application.model.User;
import application.repository.ProjectRepository;
import application.repository.TaskRepository;
import application.repository.UserRepository;
import application.service.TelegramService;
import application.telegram.TelegramBot;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    private static final String CANT_FIND_TASK_BY_ID = "Can't find a task with id: ";
    private static final String PRIORITY_HIGH = "HIGH";
    private static final String PRIORITY_MEDIUM = "MEDIUM";
    private static final String PRIORITY_LOW = "LOW";
    private static final String STATUS_IN_COMPLETED = "COMPLETED";
    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private static final String TASK1_NAME = "Task 1";
    private static final Integer FIRST_TASK = 1;
    private static final String TASK1_DESCRIPTION = "Description 1";
    private static final Integer TASK1_DUE_DATE = 7;
    private static final String TASK2_NAME = "Task 2";
    private static final String TASK2_DESCRIPTION = "Description 2";
    private static final Integer TASK2_DUE_DATE = 5;
    private static final String UPDATED_TASK2_NAME = "Updated task 2";
    private static final String UPDATED_TASK2_DESCRIPTION = "Updated description 2";
    private static final Integer UPDATED_TASK2_DUE_DATE = 8;
    private static final String TASK3_NAME = "Task 3";
    private static final String TASK3_DESCRIPTION = "Description 3";
    private static final Integer TASK3_DUE_DATE = 6;
    private static final long FIRST_PROJECT_ID = 1L;
    private static final long FIRST_TASK_ID = 1L;
    private static final long FIRST_USER_ID = 1L;
    private static final long SECOND_TASK_ID = 2L;
    private static final long SECOND_PROJECT_ID = 2L;
    private static final long SECOND_USER_ID = 2L;
    private static final long THIRD_TASK_ID = 4L;
    private static final long INVALID_ID = 20L;
    private static final int ONE_TIME = 1;
    private static final int ONE_EXPECTED = 1;
    private static Task task2;
    private static TaskResponseDto taskResponseDto2;
    private static List<Task> tasks;
    private static List<TaskResponseDto> taskResponseDtos;
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TelegramService telegramService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @BeforeAll
    public static void beforeAll() {
        Task task1 = new Task().setId(FIRST_TASK_ID)
                .setName(TASK1_NAME)
                .setDescription(TASK1_DESCRIPTION)
                .setPriority(Task.Priority.HIGH)
                .setStatus(Task.Status.IN_PROGRESS)
                .setDueDate(LocalDate.now().plusDays(TASK1_DUE_DATE))
                .setProject(new Project().setId(FIRST_PROJECT_ID))
                .setAssignee(new User().setId(FIRST_USER_ID));

        task2 = new Task().setId(SECOND_TASK_ID)
                .setName(TASK2_NAME)
                .setDescription(TASK2_DESCRIPTION)
                .setPriority(Task.Priority.MEDIUM)
                .setStatus(Task.Status.COMPLETED)
                .setDueDate(LocalDate.now().plusDays(TASK2_DUE_DATE))
                .setProject(new Project().setId(SECOND_PROJECT_ID))
                .setAssignee(new User().setId(SECOND_USER_ID));

        tasks = List.of(task1, task2);

        TaskResponseDto taskResponseDto1 = new TaskResponseDto()
                .setId(task1.getId())
                .setName(task1.getName())
                .setDescription(task1.getDescription())
                .setPriority(PRIORITY_HIGH)
                .setStatus(STATUS_IN_PROGRESS)
                .setDueDate(task1.getDueDate())
                .setProjectId(FIRST_PROJECT_ID)
                .setAssigneeId(FIRST_USER_ID);

        taskResponseDto2 = new TaskResponseDto()
                .setId(task2.getId())
                .setName(task2.getName())
                .setDescription(task2.getDescription())
                .setPriority(PRIORITY_MEDIUM)
                .setStatus(STATUS_IN_COMPLETED)
                .setDueDate(task2.getDueDate())
                .setProjectId(SECOND_PROJECT_ID)
                .setAssigneeId(SECOND_USER_ID);

        taskResponseDtos = List.of(taskResponseDto1, taskResponseDto2);
    }

    @Test
    @DisplayName("""
            Verify getAll() method
            """)
    void getAll_ValidProjectId_ReturnOneTask() {
        // when
        when(taskRepository.getAllByProjectId(SECOND_PROJECT_ID))
                .thenReturn(List.of(task2));
        when(taskMapper.toResponseDto(task2))
                .thenReturn(taskResponseDto2);

        // then
        List<TaskResponseDto> expected
                = List.of(taskResponseDto2);
        List<TaskResponseDto> actual
                = taskServiceImpl.getAll(SECOND_PROJECT_ID);
        assertEquals(ONE_EXPECTED, actual.size());
        assertEquals(expected, actual);
        verify(taskRepository, times(ONE_TIME)).getAllByProjectId(SECOND_PROJECT_ID);
    }

    @Test
    @DisplayName("""
            Verify createTask() method with correct requestDto
            """)
    void createTask_ValidTaskRequest_ReturnTaskDto() {
        // given
        TaskRequestDto taskRequestDto3 = new TaskRequestDto()
                .setName(TASK3_NAME)
                .setDescription(TASK3_DESCRIPTION)
                .setPriority(PRIORITY_HIGH)
                .setStatus(STATUS_IN_COMPLETED)
                .setDueDate(LocalDate.now().plusDays(TASK3_DUE_DATE))
                .setProjectId(SECOND_PROJECT_ID)
                .setAssigneeId(SECOND_USER_ID);
        Task task3 = new Task()
                .setId(THIRD_TASK_ID)
                .setName(taskRequestDto3.getName())
                .setDescription(taskRequestDto3.getDescription())
                .setPriority(Task.Priority.HIGH)
                .setStatus(Task.Status.COMPLETED)
                .setDueDate(taskRequestDto3.getDueDate())
                .setProject(new Project().setId(SECOND_PROJECT_ID))
                .setAssignee(new User().setId(SECOND_USER_ID));

        TaskResponseDto taskResponseDto3 = new TaskResponseDto()
                .setId(task3.getId())
                .setName(task3.getName())
                .setDescription(task3.getDescription())
                .setPriority(PRIORITY_HIGH)
                .setStatus(STATUS_IN_COMPLETED)
                .setDueDate(LocalDate.now().plusDays(TASK3_DUE_DATE))
                .setProjectId(SECOND_PROJECT_ID)
                .setAssigneeId(SECOND_USER_ID);

        Project project2 = new Project().setId(SECOND_PROJECT_ID);
        when(projectRepository.findById(SECOND_PROJECT_ID)).thenReturn(Optional.of(project2));

        User user2 = new User().setId(SECOND_USER_ID);
        when(userRepository.findById(SECOND_USER_ID)).thenReturn(Optional.of(user2));

        // when
        when(taskMapper.toEntity(taskRequestDto3)).thenReturn(task3);
        when(taskRepository.save(task3)).thenReturn(task3);
        Mockito.lenient().doNothing().when(telegramBot).prepareAndSendMessage(anyLong(),
                anyString(), anyLong());
        when(taskMapper.toResponseDto(task3)).thenReturn(taskResponseDto3);

        // then
        TaskResponseDto actual = taskServiceImpl.createTask(taskRequestDto3);
        assertNotNull(actual);
        assertEquals(taskResponseDto3, actual);
        verify(taskRepository, times(ONE_TIME)).save(any(Task.class));
    }

    @Test
    @DisplayName("""
            Verify getTaskById() method with valid taskId
            """)
    void findTaskById_ValidTaskId_ReturnTaskDto() {
        // when
        when(taskRepository
                .findById(FIRST_TASK_ID))
                .thenReturn(Optional.ofNullable(tasks.get(FIRST_TASK)));
        when(taskMapper.toResponseDto(tasks.get(FIRST_TASK)))
                .thenReturn(taskResponseDtos.get(FIRST_TASK));

        // then
        TaskResponseDto actual = taskServiceImpl.getTaskById(FIRST_TASK_ID);
        assertNotNull(actual);
        assertEquals(taskResponseDtos.get(FIRST_TASK), actual);
        verify(taskRepository, times(ONE_TIME)).findById(FIRST_TASK_ID);
    }

    @Test
    @DisplayName("""
            Verify getTaskById() method with invalid taskId
            """)
    void findTaskById_InvalidTaskId_ThrowException() {
        // when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> taskServiceImpl.getTaskById(INVALID_ID));

        // then
        String expected = CANT_FIND_TASK_BY_ID + INVALID_ID;
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(taskRepository, times(ONE_TIME)).findById(INVALID_ID);
    }

    @Test
    @DisplayName("""
            Verify updateTaskById() method with correct requestDto
            """)
    void updateTaskById_ValidTaskRequest_ReturnTaskDto() {
        // given
        TaskRequestDto updatedTaskRequestDto2 = new TaskRequestDto()
                .setName(UPDATED_TASK2_NAME)
                .setDescription(UPDATED_TASK2_DESCRIPTION)
                .setPriority(PRIORITY_LOW)
                .setStatus(STATUS_IN_PROGRESS)
                .setDueDate(LocalDate.now().plusDays(UPDATED_TASK2_DUE_DATE))
                .setProjectId(FIRST_PROJECT_ID)
                .setAssigneeId(SECOND_USER_ID);
        Task updatedTask2 = new Task()
                .setId(SECOND_TASK_ID)
                .setName(updatedTaskRequestDto2.getName())
                .setDescription(updatedTaskRequestDto2.getDescription())
                .setPriority(Task.Priority.LOW)
                .setStatus(Task.Status.IN_PROGRESS)
                .setDueDate(updatedTaskRequestDto2.getDueDate())
                .setProject(new Project().setId(FIRST_PROJECT_ID))
                .setAssignee(new User().setId(SECOND_USER_ID));

        TaskResponseDto updatedTaskResponseDto2 = new TaskResponseDto()
                .setId(task2.getId())
                .setName(task2.getName())
                .setDescription(task2.getDescription())
                .setPriority(PRIORITY_LOW)
                .setStatus(STATUS_IN_PROGRESS)
                .setDueDate(LocalDate.now().plusDays(UPDATED_TASK2_DUE_DATE))
                .setProjectId(FIRST_PROJECT_ID)
                .setAssigneeId(SECOND_USER_ID);

        // when

        when(taskRepository.findById(SECOND_TASK_ID)).thenReturn(Optional.of(task2));
        User user2 = new User().setId(SECOND_USER_ID);
        when(userRepository.findById(SECOND_USER_ID)).thenReturn(Optional.of(user2));
        when(taskMapper.toEntity(updatedTaskRequestDto2)).thenReturn(updatedTask2);
        when(taskRepository.save(updatedTask2)).thenReturn(updatedTask2);
        Mockito.lenient().doNothing().when(telegramBot).prepareAndSendMessage(anyLong(),
                anyString(), anyLong());
        when(taskMapper.toResponseDto(updatedTask2)).thenReturn(updatedTaskResponseDto2);

        // then
        TaskResponseDto actual = taskServiceImpl
                .updateTaskById(SECOND_TASK_ID, updatedTaskRequestDto2);
        assertNotNull(actual);
        assertEquals(updatedTaskResponseDto2, actual);
        verify(taskRepository, times(ONE_TIME)).findById(SECOND_TASK_ID);
        verify(taskRepository, times(ONE_TIME)).save(updatedTask2);
    }

    @Test
    @DisplayName("Verify deleteTaskById() deletes the task by ID")
    void deleteTaskById_TaskExists_TaskDeleted() {
        // given
        Long taskId = FIRST_TASK_ID;

        // when
        taskServiceImpl.deleteTaskById(taskId);

        // then
        verify(taskRepository, times(ONE_TIME)).deleteById(taskId);
    }
}
