package application.service;

import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseRegistrationDto;

public interface UserService {
    UserResponseRegistrationDto register(UserRequestRegistrationDto userRequestRegistrationDto);

    UserResponseDto updateRole(Long id, UpdateRoleRequestDto roleRequestDto);

    UserResponseDto getProfile(Long id);

    UserResponseDto updateProfileInfo(Long id, UpdateProfileRequestDto profileRequestDto);
}
