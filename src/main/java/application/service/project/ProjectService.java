package application.service.project;

import application.dto.project.ProjectRequestDto;
import application.dto.project.ProjectResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    List<ProjectResponseDto> findAll(Pageable pageable);

    ProjectResponseDto findById(Long id);

    ProjectResponseDto save(ProjectRequestDto requestDto);

    ProjectResponseDto update(ProjectRequestDto requestDto, Long id);

    void delete(Long id);
}
