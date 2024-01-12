package application.service;

import application.dto.user.UpdateProfileRequestDto;
import application.dto.user.UpdateRoleRequestDto;
import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRequestRegistrationDto userRequestRegistrationDto);

    UserResponseDto updateRole(Long id, UpdateRoleRequestDto roleRequestDto);

    UserResponseDto getProfile(Long id);

    UserResponseDto updateProfileInfo(Long id, UpdateProfileRequestDto profileRequestDto);
}
