package application.controller;

import application.dto.label.LabelRequestDto;
import application.dto.label.LabelResponseDto;
import application.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/labels")
@RequiredArgsConstructor
@Tag(name = "Label management", description = "Endpoints for label management")
public class LabelController {
    private final LabelService labelService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new label",
            description = "Endpoint for creating a new label")
    public LabelResponseDto create(@RequestBody @Valid LabelRequestDto labelRequestDto) {
        return labelService.create(labelRequestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all labels",
            description = "Endpoint for getting a list of all available labels")
    public Set<LabelResponseDto> getAll(Pageable pageable) {
        return labelService.getAllByIds(pageable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a label",
            description = "Endpoint for updating data about the existing label by ID")
    public LabelResponseDto update(
            @RequestBody @Valid LabelRequestDto labelRequestDto,
            @PathVariable Long id
    ) {
        return labelService.update(labelRequestDto, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a label",
            description = "Endpoint for marking existing label for deletion by ID")
    public void delete(@PathVariable Long id) {
        labelService.deleteById(id);
    }
}
