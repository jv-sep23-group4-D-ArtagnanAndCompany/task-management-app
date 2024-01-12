package application.dto.attachment;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AttachmentResponseDto {
    private Long id;
    private String fileName;
    private String dropBoxFileId;
    private String taskId;
}
