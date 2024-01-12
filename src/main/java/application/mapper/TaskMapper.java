package application.mapper;

import application.config.MapperConfig;
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
    TaskResponseDto toDto(Task task);

    @Named("getStatusFromEnum")
    default String getStatusFromEnum(Task.Status status) {
        return status.toString();
    }
    @Named("getPriorityFromEnum")
    default String getPriorityFromEnum(Task.Priority priority) {
        return priority.toString();
    }
}
