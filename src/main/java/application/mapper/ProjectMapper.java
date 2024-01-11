package application.mapper;

import application.config.MapperConfig;
import application.dto.project.ProjectRequestDto;
import application.dto.project.ProjectResponseDto;
import application.dto.project.ProjectUpdateDto;
import application.model.Project;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ProjectMapper {
    ProjectResponseDto toDto(Project project);

    Project toEntity(ProjectRequestDto requestDto);

    Project toEntity(ProjectUpdateDto requestDto);

    List<ProjectResponseDto> toDtoList(List<Project> projects);
}
