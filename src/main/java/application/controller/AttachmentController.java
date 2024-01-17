package application.controller;

import application.dto.attachment.FileUploadResponseDto;
import application.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@Tag(name = "Attachments management", description = "Endpoints for attachments management")
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload a file",
            description = "Endpoint for uploading a file")
    public FileUploadResponseDto upload(@Validated @RequestParam(name = "file")
                                                      MultipartFile multipartFile,
                                        @RequestParam Long taskId) {
        return attachmentService.upload(taskId, multipartFile);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all attachments by taskId",
            description = "Endpoint for getting all attachments by taskId")
    public void retrieveAllByTaskId(@RequestParam Long taskId,
                                                            HttpServletResponse response) {
        attachmentService.retrieveAllByTaskId(taskId, response);
    }
}
