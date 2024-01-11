package application.service.comment;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import java.util.List;

public interface CommentService {
    List<CommentResponseDto> getCommentsByTaskId(Long taskId);

    CommentResponseDto save(CommentRequestDto requestDto);
}
