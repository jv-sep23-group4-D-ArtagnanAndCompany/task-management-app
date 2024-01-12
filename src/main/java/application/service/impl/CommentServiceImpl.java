package application.service.impl;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.CommentMapper;
import application.model.Comment;
import application.model.Task;
import application.model.User;
import application.repository.CommentRepository;
import application.repository.TaskRepository;
import application.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;

    @Override
    public List<CommentResponseDto> getCommentsByTaskId(Long taskId) {
        return commentMapper.toDtoList(commentRepository.findAllByTaskId(taskId));
    }

    @Override
    public CommentResponseDto save(CommentRequestDto requestDto, User user) {
        Comment comment = commentMapper.toEntity(requestDto);
        Long taskId = requestDto.getTaskId();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find task with given id: " + taskId));
        comment.setTask(task);
        comment.setUser(user);
        return commentMapper.toDto(commentRepository.save(comment));
    }
}
