package application.dto.comment;

import application.dto.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentRequestDto {
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String text;
    @NotNull
    @Positive(message = ValidationMessages.POSITIVE)
    private Long taskId;
}
