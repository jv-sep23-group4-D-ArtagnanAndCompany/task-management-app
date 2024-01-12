package application.service;

import application.dto.project.ProjectRequestDto;
import application.dto.project.ProjectResponseDto;
import application.dto.project.ProjectUpdateDto;
import application.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    List<ProjectResponseDto> findAll(Long id, Pageable pageable);

    ProjectResponseDto findById(Long userId, Long id);

    ProjectResponseDto save(ProjectRequestDto requestDto, User user);

    ProjectResponseDto update(ProjectUpdateDto requestDto, Long id, Long userId);

    void delete(Long id);
}
