package application.mapper;

import application.config.MapperConfig;
import application.dto.project.ProjectRequestDto;
import application.dto.project.ProjectResponseDto;
import application.model.Project;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ProjectMapper {
    ProjectResponseDto toResponseDto(Project project);

    Project toEntity(ProjectRequestDto requestDto);

    List<ProjectResponseDto> toResponseDtoList(List<Project> projects);
}
