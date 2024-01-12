package application.dto.attachment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AttachmentRequestDto {
    @NotBlank
    private String fileName;
    @NotNull
    private Long taskId;
}
