package application.service;

import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRequestRegistrationDto userRequestRegistrationDto);
}
