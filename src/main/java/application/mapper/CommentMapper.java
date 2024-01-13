package application.mapper;

import application.config.MapperConfig;
import application.dto.comment.CommentRequestDto;
import application.dto.comment.CommentResponseDto;
import application.model.Comment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "taskId", source = "task.id")
    CommentResponseDto toDto(Comment comment);

    Comment toEntity(CommentRequestDto requestDto);

    List<CommentResponseDto> toDtoList(List<Comment> comments);
}
