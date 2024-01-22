package application.service;

import application.dto.attachment.FileUploadResponseDto;
import application.model.Attachment;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DropBoxService {
    FileUploadResponseDto uploadFileToDropBox(MultipartFile multipartFile);

    void downloadFromDropBox(Attachment attachment, HttpServletResponse response);
}
