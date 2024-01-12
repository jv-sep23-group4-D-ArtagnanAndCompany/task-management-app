package application.service.impl;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.mapper.CommentMapper;
import application.model.Comment;
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
    public List<CommentResponseDto> getCommentsByTaskId(Long taskId) {
        return commentMapper.toDtoList(commentRepository.findAllByTaskId(taskId));
    }

    @Override
    public CommentResponseDto save(CommentRequestDto requestDto) {
        Comment comment = commentMapper.toEntity(requestDto);
        return commentMapper.toDto(commentRepository.save(comment));
    }
}