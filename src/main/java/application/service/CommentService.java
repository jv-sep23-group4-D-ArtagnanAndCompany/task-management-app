package application.service;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.model.User;
import java.util.List;

public interface CommentService {
    List<CommentResponseDto> getCommentsByTaskId(Long taskId);

    CommentResponseDto save(CommentRequestDto requestDto, User user);
}
