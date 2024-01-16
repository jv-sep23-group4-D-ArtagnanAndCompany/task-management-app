package application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.mapper.CommentMapper;
import application.model.Comment;
import application.model.Task;
import application.model.User;
import application.repository.CommentRepository;
import application.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Get all user comments for the task")
    public void getCommentsByTaskId_WithComment_ReturnList() {
        User user = new User();
        user.setId(3L);
        Task task = new Task();
        task.setId(2L);
        LocalDateTime timeStamp = LocalDateTime.now();
        Comment comment = new Comment()
                .setId(1L)
                .setTask(task)
                .setUser(user)
                .setTimeStamp(timeStamp);
        comment.setText("Some comment to task");
        List<Comment> comments = List.of(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto()
                .setId(1L)
                .setTaskId(comment.getTask().getId())
                .setUserId(comment.getUser().getId())
                .setText(comment.getText())
                .setTimeStamp(comment.getTimeStamp());
        List<CommentResponseDto> expected = List.of(commentResponseDto);

        Mockito.when(commentRepository.findAllByTaskIdAndUserId(task.getId(), user.getId()))
                .thenReturn(comments);
        Mockito.when(commentMapper.toResponseDtoList(comments))
                .thenReturn(expected);

        List<CommentResponseDto> actual =
                commentService.getCommentsByTaskId(task.getId(), user.getId());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Create user comment for task")
    public void save_WithUser_ReturnCommentResponseDto() {
        Task task = new Task();
        task.setId(2L);
        CommentRequestDto requestDto = new CommentRequestDto()
                .setText("Beautiful comment")
                .setTaskId(task.getId());
        User user = new User()
                .setId(3L);
        LocalDateTime timeStamp = LocalDateTime.now();
        Comment comment = new Comment()
                .setId(2L)
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
        Mockito.when(commentMapper.toEntity(requestDto))
                .thenReturn(comment);
        Mockito.when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));
        Mockito.when(commentRepository.save(comment))
                        .thenReturn(comment);
        Mockito.when(commentMapper.toDto(comment)).thenReturn(expected);
        CommentResponseDto actual = commentService.save(requestDto, user);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
