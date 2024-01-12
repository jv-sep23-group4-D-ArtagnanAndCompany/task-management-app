package application.controller;

import application.dto.attachment.AttachmentRequestDto;
import application.dto.attachment.AttachmentResponseDto;
import application.dto.drop_box.DropBoxFileGetDto;
import application.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "Attachments management", description = "Endpoints for attachments management")
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;
    @PostMapping(/{})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new attachment",
            description = "Endpoint for creating a new attachment")
    public AttachmentResponseDto createAttachment(MultipartFile multipartFile) {
        return attachmentService.createAttachment(requestDto);
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all attachments by taskId",
            description = "Endpoint for getting all attachments by taskId")
    public List<DropBoxFileGetDto> retrieveAllByTaskId(@PathVariable Long taskId) {
        return attachmentService.retrieveAllByTaskId(taskId);
    }
}
