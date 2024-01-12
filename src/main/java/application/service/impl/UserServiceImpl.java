package application.service.impl;

import application.dto.user.UpdateProfileRequestDto;
import application.dto.user.UpdateRoleRequestDto;
import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseDto;
import application.exception.EntityNotFoundException;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.Role;
import application.model.User;
import application.repository.UserRepository;
import application.service.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Long USER_ROLE_ID = 2L;
    private static final String CANT_FIND_BY_ID = "User with id %s is not found";
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

    @Override
    public UserResponseDto updateRole(Long id, UpdateRoleRequestDto roleRequestDto) {
        User userFromDb = getUserFromDb(id);
        Role role = new Role();
        Set<Role> roleSet = userFromDb.getRoleSet();
        if (roleSet.add(role.setRoleName(roleRequestDto.getRole().getRoleName()))) {
            userFromDb.setRoleSet(roleSet);
        }
        return userMapper.toDto(userRepository.save(userFromDb));
    }

    @Override
    public UserResponseDto getProfile(Long id) {
        return userMapper.toDto(getUserFromDb(id));
    }

    @Override
    public UserResponseDto updateProfileInfo(Long id, UpdateProfileRequestDto profileRequestDto) {
        BeanUtils.copyProperties(userRepository.findById(id), profileRequestDto);
        return null;
    }

    private User getUserFromDb(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format(CANT_FIND_BY_ID, id)));
    }
}
