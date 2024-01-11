package application.service.comment.impl;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.mapper.CommentMapper;
import application.model.Comment;
import application.model.User;
import application.repository.CommentRepository;
import application.service.comment.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentResponseDto> getCommentsByTaskId(Long taskId, Long userId) {
        return commentMapper.toDtoList(commentRepository.findAllByTaskId(taskId, userId));
    }

    @Override
    public CommentResponseDto save(CommentRequestDto requestDto, User user) {
        Comment comment = commentMapper.toEntity(requestDto);
        comment.setUser(user);
//        TODO SET TASK HERE
        return commentMapper.toDto(commentRepository.save(comment));
    }
}
