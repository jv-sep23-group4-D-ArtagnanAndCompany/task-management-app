package application.controller;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comments management", description = "Endpoints for managing comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/?taskId={taskId}")
    @Operation(summary = "Get all comments to the particular task by id")
    public List<CommentResponseDto> getCommentByTaskId(@PathVariable Long taskId) {
        return commentService.getCommentsByTaskId(taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new comment")
    public CommentResponseDto createNewComment(@RequestBody CommentRequestDto requestDto) {
        return commentService.save(requestDto);
    }
}
