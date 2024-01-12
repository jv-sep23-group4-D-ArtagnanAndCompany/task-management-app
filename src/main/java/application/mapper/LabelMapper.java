package application.mapper;

import application.config.MapperConfig;
import application.dto.label.LabelRequestDto;
import application.dto.label.LabelResponseDto;
import application.model.Label;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface LabelMapper {
    LabelResponseDto toResponseDto(Label label);

    Label toEntity(LabelRequestDto labelRequestDto);
}
