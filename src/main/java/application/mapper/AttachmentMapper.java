package application.mapper;

import application.config.MapperConfig;
import application.dto.attachment.AttachmentRequestDto;
import application.dto.attachment.AttachmentResponseDto;
import application.model.Attachment;
import application.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface AttachmentMapper {
    @Mapping(target = "task", source = "taskId", qualifiedByName = "getTaskById")
    Attachment toEntity(AttachmentRequestDto attachmentRequestDto);
    @Mapping(target = "taskId", source = "task.id")
    AttachmentResponseDto toResponseDto(Attachment attachment);

    @Named("getTaskById")
    default Task getTaskById(Long taskId) {
        return new Task().setId(taskId);
    }
}
