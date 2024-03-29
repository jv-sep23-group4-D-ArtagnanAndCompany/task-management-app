package application.controller;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.model.User;
import application.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comments management", description = "Endpoints for comments management")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "Get all comments to the particular task by id",
            description = "Endpoint for getting all comments to the particular task by id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<CommentResponseDto> getCommentsByTaskId(@RequestParam Long taskId,
                                                       Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return commentService.getCommentsByTaskId(taskId, user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new comment",
            description = "Endpoint for creating a new comment")
    @PreAuthorize("hasRole('ADMIN')")
    public CommentResponseDto createNewComment(@RequestBody @Valid CommentRequestDto requestDto,
                                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return commentService.save(requestDto, user);
    }
}
