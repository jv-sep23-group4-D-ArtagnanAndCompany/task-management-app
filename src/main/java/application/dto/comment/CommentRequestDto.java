package application.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotBlank(message = "Cannot add an empty comment")
    private String text;
}
