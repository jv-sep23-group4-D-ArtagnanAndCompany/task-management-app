package application.service.impl;

import application.client.DropBoxFileClient;
import application.dto.attachment.AttachmentRequestDto;
import application.dto.attachment.AttachmentResponseDto;
import application.dto.drop_box.DropBoxFileGetDto;
import application.mapper.AttachmentMapper;
import application.model.Attachment;
import application.repository.AttachmentRepository;
import application.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final DropBoxFileClient dropBoxFileClient;

    @Override
    public AttachmentResponseDto createAttachment(AttachmentRequestDto attachmentRequestDto) {
        Attachment attachment = attachmentMapper.toEntity(attachmentRequestDto);
        String dropBoxFileId = dropBoxFileClient
                .uploadFileToDropBoxFile(attachment.getFileName()).getId();
        attachment.setDropBoxFileId(dropBoxFileId);
        return attachmentMapper.toResponseDto(attachmentRepository.save(attachment));
    }

    @Override
    public List<DropBoxFileGetDto> retrieveAllByTaskId(Long taskId) {
        return attachmentRepository
                .findAllByTaskId(taskId)
                .stream()
                .map(a -> dropBoxFileClient.getDto(a.getDropBoxFileId()))
                .toList();
    }
}