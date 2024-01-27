package application.service.impl;

import application.dto.attachment.FileUploadResponseDto;
import application.mapper.AttachmentMapper;
import application.model.Attachment;
import application.model.Task;
import application.repository.AttachmentRepository;
import application.service.AttachmentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final DropBoxServiceImpl dropBoxServiceImpl;

    @Override
    @Transactional
    public FileUploadResponseDto upload(Long taskId, MultipartFile multipartFile) {
        FileUploadResponseDto fileUploadResponseDto = dropBoxServiceImpl
                .uploadFileToDropBox(multipartFile);
        Attachment attachment = attachmentMapper.toEntity(fileUploadResponseDto)
                .setTask(new Task().setId(taskId));
        Attachment savedAttachment = attachmentRepository.save(attachment);
        return attachmentMapper.toResponseDto(savedAttachment);
    }

    @Override
    public void retrieveAllByTaskId(Long taskId,
                                    HttpServletResponse response) {
        attachmentRepository
                .findAllByTaskId(taskId)
                .stream()
                .forEach(a -> dropBoxServiceImpl.downloadFromDropBox(a, response));
    }
}
