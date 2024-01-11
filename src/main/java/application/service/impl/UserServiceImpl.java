package application.service.impl;

import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseRegistrationDto;
import application.exception.EntityNotFoundException;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.Role;
import application.model.User;
import application.repository.RoleRepository;
import application.repository.UserRepository;
import application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Long USER_ID = 2L;
    private static final String EXCEPTION_ROLE = "Can't find role by id ";
    private static final String EXCEPTION = "User with email %s is already created";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserResponseRegistrationDto register(UserRequestRegistrationDto userRequestRegistrationDto) {
        if (userRepository.findByEmail(userRequestRegistrationDto.getEmail()).isPresent()) {
            throw new RegistrationException(EXCEPTION + userRequestRegistrationDto.getEmail());
        }
        User user = userMapper.toEntity(userRequestRegistrationDto);
        user.setPassword(passwordEncoder.encode(userRequestRegistrationDto.getPassword()));
        Role userRole = roleRepository.findById(USER_ID).orElseThrow(()
                -> new EntityNotFoundException(EXCEPTION_ROLE + USER_ID));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoleSet(roles);
        return userMapper.toDto(userRepository.save(user));
    }
}
