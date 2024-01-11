package application.controller;

import application.dto.project.ProjectRequestDto;
import application.dto.project.ProjectResponseDto;
import application.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects management", description = "Endpoints for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "Get all projects")
    public List<ProjectResponseDto> findAllProjects(Pageable pageable) {
        return projectService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project details by id")
    public ProjectResponseDto findProjectById(@PathVariable Long id) {
        return projectService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponseDto createNewProject(@RequestBody @Valid ProjectRequestDto requestDto) {
        return projectService.save(requestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project by id")
    public ProjectResponseDto updateProjectById(@PathVariable Long id,
                                                @RequestBody ProjectRequestDto requestDto) {
        return projectService.update(requestDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete project by id")
    public void deleteProjectById(@PathVariable Long id) {
        projectService.delete(id);
    }
}
