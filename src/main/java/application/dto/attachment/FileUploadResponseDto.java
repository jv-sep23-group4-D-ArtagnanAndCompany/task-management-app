package application.dto.attachment;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FileUploadResponseDto {
    private Long id;
    private LocalDateTime uploadDate;
    private String fileName;
    private String dropBoxFileId;
    private Long taskId;
}
