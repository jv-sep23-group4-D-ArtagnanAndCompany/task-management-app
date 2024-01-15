package application.controller;

import application.dto.project.ProjectRequestDto;
import application.dto.project.ProjectResponseDto;
import application.dto.project.ProjectUpdateDto;
import application.model.User;
import application.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@Tag(name = "Projects management", description = "Endpoints for project management")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "Get all projects",
            description = "Endpoint for getting all user projects")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ProjectResponseDto> findAllProjects(Authentication authentication,
                                                    Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return projectService.findAll(user.getId(), pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project details by id",
    description = "Endpoint for getting all project details by id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ProjectResponseDto findProjectById(Authentication authentication,
                                              @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return projectService.findById(user.getId(), id);
    }

    @PostMapping
    @Operation(summary = "Create a new project",
            description = "Endpoint for creating a new project")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ProjectResponseDto createNewProject(@RequestBody @Valid ProjectRequestDto requestDto,
                                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return projectService.save(requestDto, user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project by id",
            description = "Endpoint for updating a project by id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ProjectResponseDto updateProjectById(@PathVariable Long id,
                                                @RequestBody ProjectUpdateDto requestDto,
                                                Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return projectService.update(requestDto, id, user.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a project by id",
            description = "Endpoint for deleting a project by id")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProjectById(@PathVariable Long id) {
        projectService.delete(id);
    }
}
