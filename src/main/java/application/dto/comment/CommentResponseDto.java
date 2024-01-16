package application.dto.comment;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentResponseDto {
    private Long id;
    private Long taskId;
    private Long userId;
    private String text;
    private LocalDateTime timeStamp;
}
