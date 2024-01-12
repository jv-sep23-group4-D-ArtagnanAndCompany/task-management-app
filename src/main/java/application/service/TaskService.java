package application.service;

import application.dto.task.TaskRequestDto;
import application.dto.task.TaskResponseDto;
import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskRequestDto);

    List<TaskResponseDto> getAll(Long projectId);

    TaskResponseDto updateTaskById(Long taskId, TaskRequestDto taskRequestDto);

    TaskResponseDto getTaskById(Long taskId);

    void deleteTaskById(Long taskId);
}
