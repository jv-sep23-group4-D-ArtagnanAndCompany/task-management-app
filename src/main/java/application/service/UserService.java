package application.service;

import application.dto.UpdateProfileRequestDto;
import application.dto.UpdateRoleRequestDto;
import application.dto.UserResponseDto;

public interface UserService {
    UserResponseDto updateRole(Long id, UpdateRoleRequestDto roleRequestDto);

    UserResponseDto getProfile(Long id);

    UserResponseDto updateProfileInfo(Long id, UpdateProfileRequestDto profileRequestDto);
}
