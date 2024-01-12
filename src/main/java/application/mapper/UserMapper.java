package application.mapper;

import application.config.MapperConfig;
import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseRegistrationDto;
import application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequestRegistrationDto registrationDto);

    UserResponseRegistrationDto toDto(User user);
}
