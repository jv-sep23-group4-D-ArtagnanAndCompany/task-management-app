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
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private static final String CANT_FIND_PROJECT_BY_ID = "Can't find a project with id: ";
    private static final String CANT_FIND_USER_BY_ID = "Can't find a user with id: ";
    private static final String CANT_FIND_TASK_BY_ID = "Can't find a task with id: ";
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Task task = new Task();
        Task createdTask = savedTask(taskRequestDto, task);
        return taskMapper.toResponseDto(createdTask);
    }

    @Override
    public List<TaskResponseDto> getAll(Long projectId) {
        return taskRepository.getAllByProjectId(projectId).stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    @Override
    public TaskResponseDto updateTaskById(Long taskId, TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(CANT_FIND_TASK_BY_ID + taskId));
        Task updatedTask = savedTask(taskRequestDto, task);
        return taskMapper.toResponseDto(updatedTask);
    }

    @Override
    public TaskResponseDto getTaskById(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return taskMapper
                .toResponseDto(task.orElseThrow(() ->
                        new EntityNotFoundException(CANT_FIND_TASK_BY_ID + taskId)));
    }

    @Override
    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    private Task savedTask(TaskRequestDto taskRequestDto, Task task) {
        Project project = projectRepository.findById(taskRequestDto.getProjectId()).orElseThrow(
                () -> new EntityNotFoundException(CANT_FIND_PROJECT_BY_ID
                        + taskRequestDto.getProjectId()));
        User assignee = userRepository.findById(taskRequestDto.getAssigneeId()).orElseThrow(
                () -> new EntityNotFoundException(CANT_FIND_USER_BY_ID
                        + taskRequestDto.getAssigneeId()));
        return taskRepository.save(newTask(taskRequestDto, project, assignee, task));
    }

    private Task newTask(TaskRequestDto requestDto, Project project, User assignee, Task task) {
        task.setName(requestDto.getName());
        task.setDescription(requestDto.getDescription());
        task.setPriority(Task.Priority.valueOf(requestDto.getPriority()));
        task.setStatus(Task.Status.valueOf(requestDto.getStatus()));
        task.setDueDate(requestDto.getDueDate());
        task.setProject(project);
        task.setAssignee(assignee);
        return task;
    }
}
