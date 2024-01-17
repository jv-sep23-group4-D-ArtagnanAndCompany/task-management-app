package application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.mapper.CommentMapper;
import application.model.Comment;
import application.model.Task;
import application.model.User;
import application.repository.CommentRepository;
import application.repository.TaskRepository;
import application.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    private static final Long COMMENT1_ID = 1L;
    private static final Long COMMENT2_ID = 2L;
    private static final Long TASK_ID = 2L;
    private static final Long USER_ID = 3L;
    private static final String COMMENT_TEXT = "Some comment to task";
    @Mock
    private TelegramServiceImpl telegramServiceImpl;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Get all user comments for the task")
    public void getCommentsByTaskId_WithComment_ReturnList() {
        User user = new User();
        user.setId(USER_ID);
        Task task = new Task();
        task.setId(TASK_ID);
        LocalDateTime timeStamp = LocalDateTime.now();
        Comment comment = new Comment()
                .setId(COMMENT1_ID)
                .setTask(task)
                .setUser(user)
                .setTimeStamp(timeStamp);
        comment.setText(COMMENT_TEXT);
        List<Comment> comments = List.of(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto()
                .setId(COMMENT1_ID)
                .setTaskId(comment.getTask().getId())
                .setUserId(comment.getUser().getId())
                .setText(comment.getText())
                .setTimeStamp(comment.getTimeStamp());
        List<CommentResponseDto> expected = List.of(commentResponseDto);

        when(commentRepository.findAllByTaskIdAndUserId(task.getId(), user.getId()))
                .thenReturn(comments);
        when(commentMapper.toResponseDtoList(comments))
                .thenReturn(expected);

        List<CommentResponseDto> actual =
                commentService.getCommentsByTaskId(task.getId(), user.getId());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Create user comment for task")
    public void save_WithUser_ReturnCommentResponseDto() {
        User user = new User()
                .setId(USER_ID);
        Task task = new Task()
                .setId(TASK_ID)
                .setAssignee(user);
        CommentRequestDto requestDto = new CommentRequestDto()
                .setText(COMMENT_TEXT)
                .setTaskId(task.getId());
        LocalDateTime timeStamp = LocalDateTime.now();
        Comment comment = new Comment()
                .setId(COMMENT2_ID)
                .setTask(task)
                .setUser(user)
                .setText(requestDto.getText())
                .setTimeStamp(timeStamp)
                .setDeleted(false);
        CommentResponseDto expected = new CommentResponseDto()
                .setId(comment.getId())
                .setTaskId(requestDto.getTaskId())
                .setUserId(user.getId())
                .setText(requestDto.getText());
        when(userRepository.findUserById(user.getId()))
                .thenReturn(Optional.of(user));
        when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));
        when(commentMapper.toEntity(requestDto))
                .thenReturn(comment);
        when(commentRepository.save(comment))
                        .thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(expected);
        CommentResponseDto actual = commentService.save(requestDto, user);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
