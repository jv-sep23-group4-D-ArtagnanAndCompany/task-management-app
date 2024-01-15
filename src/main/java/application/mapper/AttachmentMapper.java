package application.mapper;

import application.config.MapperConfig;
import application.dto.attachment.FileUploadResponseDto;
import application.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AttachmentMapper {
    @Mapping(target = "taskId", source = "task.id")
    FileUploadResponseDto toResponseDto(Attachment attachment);

    @Mapping(target = "uploadDate", source = "uploadDate")
    Attachment toEntity(FileUploadResponseDto fileUploadResponseDto);
}
