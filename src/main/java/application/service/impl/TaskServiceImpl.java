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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Project project = projectRepository.findById(taskRequestDto.getProjectId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find a project with id: "
                        + taskRequestDto.getProjectId()));
        User assignee = userRepository.findById(taskRequestDto.getAssigneeId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find a user with id: "
                        + taskRequestDto.getAssigneeId()));
        Task savedTask = taskRepository.save(newTask(taskRequestDto, project, assignee));

        return taskMapper.toDto(savedTask);
    }

    @Override
    public List<TaskResponseDto> getAll(Long projectId) {
        return taskRepository.getAllByProjectId(projectId).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskResponseDto updateTaskById(Long taskId, TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Can't find a task with id: " + taskId));
        task.setPriority(Task.Priority.valueOf(taskRequestDto.getPriority()));
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskResponseDto getTaskById(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return taskMapper
                .toDto(task.orElseThrow(() ->
                        new EntityNotFoundException("Can't find a task with id: " + taskId)));
    }

    @Override
    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    private Task newTask(TaskRequestDto requestDto, Project project, User assignee) {
        Task task = new Task();

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
