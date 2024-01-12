package application.mapper;

import application.config.MapperConfig;
import application.dto.user.UserProfileResponseDto;
import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseDto;
import application.model.Role;
import application.model.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequestRegistrationDto registrationDto);

    UserResponseDto toDto(User user);

    @Mapping(target = "roleIds", source = "roleSet", qualifiedByName = "getIdsByRoles")
    UserProfileResponseDto toResponseDtoWithRoles(User user);

    @Mapping(target = "roleIds", source = "roleSet", qualifiedByName = "getIdsByRoles")
    @Named(value = "getIdsByRoles")
    default Set<Long> bookByBookId(Set<Role> roleSet) {
        return roleSet.stream().map(Role::getId).collect(Collectors.toSet());
    }
}

