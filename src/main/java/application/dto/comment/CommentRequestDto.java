package application.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotBlank(message = " field cannot be empty or null")
    private String text;
    @Positive(message = " cannot be less then 1")
    private Long taskId;
}
