package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import application.dto.attachment.FileUploadResponseDto;
import application.mapper.AttachmentMapper;
import application.model.Attachment;
import application.model.Task;
import application.repository.AttachmentRepository;
import application.service.DropBoxService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceImplTest {
    @Mock
    private AttachmentRepository attachmentRepository;
    @Mock
    private AttachmentMapper attachmentMapper;
    @Mock
    private DropBoxService dropBoxService;
    @InjectMocks
    private AttachmentServiceImpl attachmentServiceImpl;

    @Test
    @DisplayName("""
            Verify upload() method
            """)
    void upload_ValidMultiPartFile_ReturnFileUploadResponseDto() {
        // given
        String fileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = "Hello world".getBytes();
        MultipartFile multipartFile
                = new MockMultipartFile(fileName, fileName, contentType, content);
        FileUploadResponseDto fileUploadResponseDto
                = new FileUploadResponseDto()
                .setFileName(fileName).setDropBoxFileId("sl.gdfgfdhfdhdhfhfdh");
        Attachment attachment = new Attachment()
                .setFileName(fileName)
                .setDropBoxFileId(fileUploadResponseDto.getDropBoxFileId())
                        .setTask(new Task().setId(1L));
        Attachment savedAttachment = new Attachment()
                .setId(1L)
                .setDropBoxFileId(attachment.getDropBoxFileId())
                .setFileName(attachment.getFileName())
                .setUploadDate(LocalDateTime.now());
        FileUploadResponseDto resultResponseDto = new FileUploadResponseDto()
                .setUploadDate(savedAttachment.getUploadDate())
                .setFileName(savedAttachment.getFileName())
                .setTaskId(attachment.getTask().getId())
                .setUploadDate(savedAttachment.getUploadDate())
                .setDropBoxFileId(savedAttachment.getDropBoxFileId());

        // when
        when(dropBoxService.uploadFileToDropBox(multipartFile))
                .thenReturn(fileUploadResponseDto);
        when(attachmentMapper.toEntity(fileUploadResponseDto)).thenReturn(attachment);
        when(attachmentRepository.save(attachment)).thenReturn(savedAttachment);
        when(attachmentMapper.toResponseDto(attachment)).thenReturn(resultResponseDto);

        // then
        FileUploadResponseDto actual = attachmentServiceImpl.upload(1L, multipartFile);
        assertNotNull(actual);
        assertEquals(resultResponseDto, actual);
    }
}
