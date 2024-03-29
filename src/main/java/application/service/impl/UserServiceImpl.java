package application.service.impl;

import application.dto.user.UpdateProfileRequestDto;
import application.dto.user.UpdateRoleRequestDto;
import application.dto.user.UserProfileResponseDto;
import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseDto;
import application.exception.EntityNotFoundException;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.Role;
import application.model.User;
import application.repository.RoleRepository;
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
    private static final String CANT_FIND_BY_ID = "User with id %s is not found";
    private static final String EMAIL_RESERVED = "User with email %s already exists";
    private static final String ROLE_NOT_FOUND = "Role with name %s isn't found";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(
            UserRequestRegistrationDto userRequestRegistrationDto) {
        if (userRepository.findUserByEmail(userRequestRegistrationDto.getEmail()).isPresent()) {
            throw new RegistrationException(String.format(EMAIL_RESERVED,
                    userRequestRegistrationDto.getEmail()));
        }
        User user = userMapper.toEntity(userRequestRegistrationDto)
                .setPassword(passwordEncoder.encode(userRequestRegistrationDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(new Role().setId(USER_ROLE_ID));
        user.setRoleSet(roles);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public UserProfileResponseDto updateRole(Long id, UpdateRoleRequestDto roleRequestDto) {
        User userFromDb = getUserFromDb(id);
        Role role = findRoleByRoleName(roleRequestDto.getRoleName());
        userFromDb.getRoleSet().add(role);
        userRepository.save(userFromDb);
        return userMapper.toResponseDtoWithRoles(userFromDb);
    }

    @Override
    public UserProfileResponseDto getProfile(Long id) {
        return userMapper.toResponseDtoWithRoles(getUserFromDb(id));
    }

    @Override
    public UserProfileResponseDto updateProfileInfo(
            Long id,
            UpdateProfileRequestDto profileRequestDto) {
        User userFromDb = getUserFromDb(id);
        User user = userMapper.toEntityFromUpdateRequest(profileRequestDto);
        user.setId(userFromDb.getId())
                .setTelegramChatId(userFromDb.getTelegramChatId())
                .setRoleSet(userFromDb.getRoleSet())
                        .setPassword(userFromDb.getPassword());
        return userMapper.toResponseDtoWithRoles(userRepository.save(user));
    }

    private User getUserFromDb(Long id) {
        return userRepository.findUserById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format(CANT_FIND_BY_ID, id)));
    }

    private Role findRoleByRoleName(Role.RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        ROLE_NOT_FOUND, roleName)));
    }
}
