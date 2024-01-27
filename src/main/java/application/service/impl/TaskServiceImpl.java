package application.service.impl;

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
import application.service.TaskService;
import application.service.TelegramService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private static final String FORMAT_UPDATE_MESSAGE
            = "A task %s has been updated with id %s";
    private static final String FORMAT_SAVE_MESSAGE = "A new task has been "
            + "added to your project %s with id %s";
    private static final String CANT_FIND_PROJECT_BY_ID = "Can't find a project with id: ";
    private static final String CANT_FIND_USER_BY_ID = "Can't find a user with id: ";
    private static final String CANT_FIND_TASK_BY_ID = "Can't find a task with id: ";
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TelegramService telegramService;

    @Override
    @Transactional
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Task createdTask = savedTask(taskRequestDto);
        telegramService.sendNotification(String.format(FORMAT_SAVE_MESSAGE,
                        createdTask.getProject().getName(), createdTask.getId()),
                createdTask.getAssignee(), createdTask.getId());
        return taskMapper.toResponseDto(createdTask);
    }

    @Override
    public List<TaskResponseDto> getAll(Long projectId) {
        return taskRepository.getAllByProjectId(projectId).stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    @Transactional
    @Override
    public TaskResponseDto updateTaskById(Long taskId, TaskRequestDto taskRequestDto) {
        Task task = findById(taskId);
        Task updatedTask = updateTask(taskRequestDto, task);
        telegramService.sendNotification(String.format(FORMAT_UPDATE_MESSAGE,
                        updatedTask.getName(), updatedTask.getId()),
                updatedTask.getAssignee(), updatedTask.getId());
        return taskMapper.toResponseDto(updatedTask);
    }

    @Override
    public TaskResponseDto getTaskById(Long taskId) {
        return taskMapper.toResponseDto(findById(taskId));
    }

    @Override
    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    private Task savedTask(TaskRequestDto taskRequestDto) {
        Project project = findProjectById(taskRequestDto.getProjectId());
        User assignee = findUserById(taskRequestDto.getAssigneeId());
        Task createdTask = newTask(taskRequestDto, project, assignee);
        Task savedTask = taskRepository.save(createdTask);
        return createdTask.setId(savedTask.getId());
    }

    private Task newTask(TaskRequestDto requestDto, Project project, User assignee) {
        return taskMapper.toEntity(requestDto).setAssignee(assignee).setProject(project);
    }

    private Task findById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(CANT_FIND_TASK_BY_ID + taskId));
    }

    private Task updateTask(TaskRequestDto taskRequestDto, Task task) {
        User assignee = findUserById(taskRequestDto.getAssigneeId());
        return taskRepository.save(taskMapper.toEntity(taskRequestDto).setId(task.getId())
                .setProject(new Project().setId(task.getProject().getId()))
                .setAssignee(assignee));
    }

    private Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(CANT_FIND_PROJECT_BY_ID
                        + projectId));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(CANT_FIND_USER_BY_ID
                        + userId));
    }
}
