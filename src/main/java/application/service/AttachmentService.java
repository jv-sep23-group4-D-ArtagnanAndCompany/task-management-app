package application.service;

import application.dto.attachment.AttachmentRequestDto;
import application.dto.attachment.AttachmentResponseDto;
import application.dto.drop_box.DropBoxFileGetDto;

import java.util.List;

public interface AttachmentService {
    AttachmentResponseDto createAttachment(AttachmentRequestDto attachmentRequestDto);
    List<DropBoxFileGetDto> retrieveAllByTaskId(Long taskId);
}
