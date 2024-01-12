package application.service;

import application.dto.UpdateProfileRequestDto;
import application.dto.UpdateRoleRequestDto;
import application.dto.UserResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.UserMapper;
import application.model.Role;
import application.model.User;
import application.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String CANT_FIND_BY_ID = "There is not found user with id ";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
                CANT_FIND_BY_ID + id));
    }
}
