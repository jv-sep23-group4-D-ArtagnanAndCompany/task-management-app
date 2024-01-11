package application.service.project.impl;

import application.dto.project.ProjectRequestDto;
import application.dto.project.ProjectResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.ProjectMapper;
import application.model.Project;
import application.repository.ProjectRepository;
import application.service.project.ProjectService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private static final String ENTITY_NOT_FOUND_ERROR_MESSAGE =
            "Can't find a project with given id: ";
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public List<ProjectResponseDto> findAll(Pageable pageable) {
        return projectMapper.toDtoList(projectRepository.findAll());
    }

    @Override
    public ProjectResponseDto findById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR_MESSAGE + id)
        );
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectResponseDto save(ProjectRequestDto requestDto) {
        Project project = projectMapper.toEntity(requestDto);
        project.setStatus(Project.Status.INITIATED);
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(3L));
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectResponseDto update(ProjectRequestDto requestDto, Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR_MESSAGE + id);
        }
        Project updatedProject = projectMapper.toEntity(requestDto);
        updatedProject.setId(id);
        return projectMapper.toDto(projectRepository.save(updatedProject));
    }

    @Override
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR_MESSAGE + id);
        }
        projectRepository.deleteById(id);
    }
}
