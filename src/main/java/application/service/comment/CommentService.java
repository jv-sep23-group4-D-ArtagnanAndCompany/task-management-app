package application.service.comment;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.model.User;
import java.util.List;

public interface CommentService {
    List<CommentResponseDto> getCommentsByTaskId(Long taskId, Long userId);

    CommentResponseDto save(CommentRequestDto requestDto, User user);
}
