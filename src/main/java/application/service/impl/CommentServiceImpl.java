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
    private static final String FORMAT_MESSAGE
            = "A new comment has been added to your task %s with id %s";
    private static final String TASK_FINDING_EXCEPTION = "Can't find task with given id: ";
    private static final String USER_FINDING_EXCEPTION = "Can't find user with given id: ";
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
        Task task = taskRepository.findById(requestDto.getTaskId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                TASK_FINDING_EXCEPTION + requestDto.getTaskId()));
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);
        User assigneeUser = userRepository.findUserById(task.getAssignee().getId())
                .orElseThrow(() -> new EntityNotFoundException(USER_FINDING_EXCEPTION
                        + task.getAssignee().getId()));
        telegramService.sendNotification(String.format(FORMAT_MESSAGE,
                        task.getName(), savedComment.getId()),
                assigneeUser, savedComment.getId());
        return commentMapper.toDto(savedComment);
    }
}
