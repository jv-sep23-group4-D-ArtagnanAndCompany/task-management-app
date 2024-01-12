package application.mapper;

import application.config.MapperConfig;
import application.dto.task.TaskResponseDto;
import application.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface TaskMapper {
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "assigneeId", source = "id")
    TaskResponseDto toDto(Task task);
}
