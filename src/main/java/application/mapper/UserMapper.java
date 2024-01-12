package application.mapper;

import application.config.MapperConfig;
import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseDto;
import application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequestRegistrationDto registrationDto);

    UserResponseDto toResponseDto(User user);
}
