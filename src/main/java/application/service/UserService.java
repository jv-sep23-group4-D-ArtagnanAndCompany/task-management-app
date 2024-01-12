package application.service;

import application.dto.user.UpdateProfileRequestDto;
import application.dto.user.UpdateRoleRequestDto;
import application.dto.user.UserProfileResponseDto;
import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRequestRegistrationDto userRequestRegistrationDto);

    UserProfileResponseDto updateRole(Long id, UpdateRoleRequestDto roleRequestDto);

    UserProfileResponseDto getProfile(Long id);

    UserProfileResponseDto updateProfileInfo(Long id, UpdateProfileRequestDto profileRequestDto);
}
