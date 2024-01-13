package application.service;

import application.dto.attachment.FileUploadResponseDto;
import java.io.InputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    FileUploadResponseDto upload(Long taskId, MultipartFile multipartFile);

    List<InputStream> retrieveAllByTaskId(Long taskId);
}
