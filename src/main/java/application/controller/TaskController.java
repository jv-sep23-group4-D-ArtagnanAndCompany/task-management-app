package application.controller;

import application.dto.task.TaskRequestDto;
import application.dto.task.TaskResponseDto;
import application.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/tasks")
@Tag(name = "Task management", description = "Endpoints for task management")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new task",
            description = "Endpoint for creating a new task")
    public TaskResponseDto createTask(@RequestBody @Valid TaskRequestDto taskRequestDto) {
        return taskService.createTask(taskRequestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all tasks",
            description = "Endpoint for getting all tasks by project id")
    public List<TaskResponseDto> getAll(@RequestParam Long projectId) {
        return taskService.getAll(projectId);
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a task status",
            description = "Endpoint for updating a task status")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto update(@PathVariable Long taskId,
                                  @RequestBody @Valid TaskRequestDto taskDto) {
        return taskService.updateTaskById(taskId, taskDto);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a task by id",
            description = "Endpoint for deleting a task by id")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTaskById(taskId);
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a task by id",
            description = "Endpoint for getting a task by id")
    public TaskResponseDto getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }
}
