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
import application.repository.UserRepository;
import application.service.CommentService;
import application.service.TelegramService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final TelegramService telegramService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;

    @Override
    public List<CommentResponseDto> getCommentsByTaskId(Long taskId, Long userId) {
        return commentMapper.toResponseDtoList(commentRepository
                .findAllByTaskIdAndUserId(taskId, userId));
    }

    @Transactional
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
        Comment savedComment = commentRepository.save(comment);
        User assigneeUser = userRepository.findUserById(task.getAssignee().getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with given id: "
                        + task.getAssignee().getId()));
        telegramService.sendNotification(String.format("A new comment has been "
                        + "added to your task %s with id %s",
                        task.getName(), savedComment.getId()),
                assigneeUser, savedComment.getId());
        return commentMapper.toDto(savedComment);
    }
}
