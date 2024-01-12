package application.mapper;

import application.config.MapperConfig;
import application.dto.health.HealthResponseDto;
import org.mapstruct.Mapper;
import org.springframework.http.HttpStatus;

@Mapper(config = MapperConfig.class)
public interface HealthMapper {
    HealthResponseDto toDto(String response, HttpStatus httpStatus);
}
