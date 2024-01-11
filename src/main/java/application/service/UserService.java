package application.service;

import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseRegistrationDto;

public interface UserService {
    UserResponseRegistrationDto register(UserRequestRegistrationDto userRequestRegistrationDto);
}
