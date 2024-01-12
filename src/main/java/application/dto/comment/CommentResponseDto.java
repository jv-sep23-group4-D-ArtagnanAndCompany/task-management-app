package application.dto.comment;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private Long id;
    private Long taskId;
    private Long userId;
    private String text;
    private LocalDateTime timeStamp;
}