package application.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CommentRequestDto {
    @Positive
    private Long taskId;
    @NotBlank(message = "Cannot add an empty comment")
    private String text;
}
