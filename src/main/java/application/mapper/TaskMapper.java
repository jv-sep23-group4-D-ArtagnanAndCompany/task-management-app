package application.mapper;

import application.config.MapperConfig;
import application.dto.task.TaskRequestDto;
import application.dto.task.TaskResponseDto;
import application.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface TaskMapper {
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "status", qualifiedByName = "getStatusFromEnum")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "getPriorityFromEnum")
    TaskResponseDto toResponseDto(Task task);

    @Mapping(target = "status", source = "status", qualifiedByName = "getStatusFromString")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "getPriorityFromString")
    Task toEntity(TaskRequestDto taskRequestDto);

    @Named("getStatusFromString")
    default Task.Status getStatusFromString(String status) {
        return Task.Status.valueOf(status);
    }

    @Named("getPriorityFromString")
    default Task.Priority getPriorityFromString(String priority) {
        return Task.Priority.valueOf(priority);
    }

    @Named("getStatusFromEnum")
    default String getStatusFromEnum(Task.Status status) {
        return status.toString();
    }

    @Named("getPriorityFromEnum")
    default String getPriorityFromEnum(Task.Priority priority) {
        return priority.toString();
    }
}
