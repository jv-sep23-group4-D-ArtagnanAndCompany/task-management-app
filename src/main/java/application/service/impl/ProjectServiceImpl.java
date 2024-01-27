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
        return projectMapper.toResponseDtoList(projectRepository.findAllByUserId(userId, pageable));
    }

    @Override
    public ProjectResponseDto findById(Long userId, Long id) {
        Project project = findProjectById(userId, id);
        return projectMapper.toResponseDto(project);
    }

    @Override
    public ProjectResponseDto save(ProjectRequestDto requestDto, User user) {
        Project project = projectMapper.toEntity(requestDto).setUser(user);
        return projectMapper.toResponseDto(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectResponseDto update(ProjectUpdateDto requestDto, Long id, Long userId) {
        if (projectRepository.findByIdAndUserId(id, userId).isPresent()) {
            Project project = projectMapper.toEntityFromUpdateDto(requestDto)
                    .setId(id).setUser(new User().setId(userId));
            return projectMapper.toResponseDto(projectRepository.save(project));
        }
        throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR_MESSAGE + id);
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    private Project findProjectById(Long userId, Long id) {
        return projectRepository.findById(userId, id).orElseThrow(
                () -> new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR_MESSAGE + id)
        );
    }
}
