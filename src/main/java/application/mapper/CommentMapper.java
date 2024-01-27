package application.mapper;

import application.config.MapperConfig;
import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.model.Comment;
import application.model.Task;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "taskId", source = "task.id")
    CommentResponseDto toDto(Comment comment);

    @Mapping(target = "task", source = "taskId", qualifiedByName = "getTaskById")
    Comment toEntity(CommentRequestDto requestDto);

    List<CommentResponseDto> toResponseDtoList(List<Comment> comments);

    @Named("getTaskById")
    default Task getTaskById(Long id) {
        return new Task().setId(id);
    }
}
