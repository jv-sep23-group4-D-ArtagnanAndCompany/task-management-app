package application.service.impl;

import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseDto;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.Role;
import application.model.User;
import application.repository.UserRepository;
import application.service.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Long USER_ROLE_ID = 2L;
    private static final String EMAIL_RESERVED = "User with email %s already exists";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(
            UserRequestRegistrationDto userRequestRegistrationDto) {
        if (userRepository.findUserByEmail(userRequestRegistrationDto.getEmail()).isPresent()) {
            throw new RegistrationException(String.format(EMAIL_RESERVED,
                    userRequestRegistrationDto.getEmail()));
        }
        User user = userMapper.toEntity(userRequestRegistrationDto);
        user.setPassword(passwordEncoder.encode(userRequestRegistrationDto.getPassword()));
        Role role = new Role().setId(USER_ROLE_ID);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoleSet(roles);
        return userMapper.toDto(userRepository.save(user));
    }
}
