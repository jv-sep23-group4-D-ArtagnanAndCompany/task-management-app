package application.service.impl;

import application.dto.attachment.FileUploadResponseDto;
import application.mapper.AttachmentMapper;
import application.model.Attachment;
import application.model.Task;
import application.repository.AttachmentRepository;
import application.service.AttachmentService;
import application.service.DropBoxService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final DropBoxService dropBoxService;

    @Override
    public FileUploadResponseDto upload(Long taskId, MultipartFile multipartFile) {
        FileUploadResponseDto fileUploadResponseDto = dropBoxService
                .uploadFileToDropBox(multipartFile);
        Attachment attachment = attachmentMapper.toEntity(fileUploadResponseDto)
                .setTask(new Task().setId(taskId));
        Attachment savedAttachment = attachmentRepository.save(attachment);
        attachment.setId(savedAttachment.getId()).setUploadDate(savedAttachment.getUploadDate());
        return attachmentMapper.toResponseDto(attachment);
    }

    @Override
    public void retrieveAllByTaskId(Long taskId,
                                    HttpServletResponse response) {
        attachmentRepository
                .findAllByTaskId(taskId)
                .stream()
                .forEach(a -> dropBoxService.downloadFromDropBox(a, response));
    }
}
