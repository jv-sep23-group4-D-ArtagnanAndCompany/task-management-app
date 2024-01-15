package application.service;

import application.dto.attachment.FileUploadResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    FileUploadResponseDto upload(Long taskId, MultipartFile multipartFile);

    void retrieveAllByTaskId(Long taskId, HttpServletResponse response);
}
