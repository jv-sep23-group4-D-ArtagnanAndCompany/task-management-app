package application.service.impl;

import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseRegistrationDto;
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
    private static final Long USER_ID = 2L;
    private static final String EXCEPTION = "User with email %s is already authenticated";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseRegistrationDto register(UserRequestRegistrationDto
                                                            userRequestRegistrationDto) {
        if (userRepository.findUserByEmail(userRequestRegistrationDto.getEmail()).isPresent()) {
            throw new RegistrationException(String.format(EXCEPTION,
                    userRequestRegistrationDto.getEmail()));
        }
        User user = userMapper.toEntity(userRequestRegistrationDto);
        user.setPassword(passwordEncoder.encode(userRequestRegistrationDto.getPassword()));
        Role role = new Role().setId(USER_ID);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoleSet(roles);
        return userMapper.toDto(userRepository.save(user));
    }
}
