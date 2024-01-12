package application.service.impl;

import application.dto.project.ProjectRequestDto;
import application.dto.project.ProjectResponseDto;
import application.dto.project.ProjectUpdateDto;
import application.exception.EntityNotFoundException;
import application.mapper.ProjectMapper;
import application.model.Project;
import application.model.User;
import application.repository.ProjectRepository;
import application.service.ProjectService;
import jakarta.transaction.Transactional;
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
    public List<ProjectResponseDto> findAll(Long userId, Pageable pageable) {
        return projectMapper.toDtoList(projectRepository.findAllByUserId(userId, pageable));
    }

    @Override
    public ProjectResponseDto findById(Long userId, Long id) {
        Project project = projectRepository.findById(userId, id).orElseThrow(
                () -> new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR_MESSAGE + id)
        );
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectResponseDto save(ProjectRequestDto requestDto, User user) {
        Project project = projectMapper.toEntity(requestDto);
        project.setStatus(Project.Status.INITIATED);
        project.setUser(user);
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectResponseDto update(ProjectUpdateDto requestDto, Long id, Long userId) {
        Project project = projectRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR_MESSAGE + id));
        project.setName(requestDto.getName());
        project.setDescription(requestDto.getDescription());
        project.setStartDate(requestDto.getStartDate());
        project.setEndDate(requestDto.getEndDate());
        project.setStatus(Project.Status.valueOf(requestDto.getStatus()));
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public void delete(Long id) {
        if (!projectRepository.existsProjectById(id)) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR_MESSAGE + id);
        }
        projectRepository.deleteById(id);
    }
}
