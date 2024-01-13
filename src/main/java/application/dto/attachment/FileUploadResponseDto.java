package application.dto.attachment;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FileUploadResponseDto {
    private Long id;
    private String fileName;
    private String dropBoxFileId;
    private Long taskId;
}
