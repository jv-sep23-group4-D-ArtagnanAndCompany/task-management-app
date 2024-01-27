package application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import application.dto.user.UpdateProfileRequestDto;
import application.dto.user.UpdateRoleRequestDto;
import application.dto.user.UserProfileResponseDto;
import application.mapper.UserMapper;
import application.model.Role;
import application.model.User;
import application.repository.RoleRepository;
import application.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private static final Long ADMIN_ROLE_ID = 1L;
    private static final Long USER_ROLE_ID = 2L;
    private static final Long USER_ID = 3L;
    private static final String USER_EMAIL = "john@gmail.com";
    private static final String USER_FIRST_NAME = "John";
    private static final String USER_LAST_NAME = "Lollipop";
    private static final String USER_USER_NAME = "John";
    private static final String UPDATE_USER_EMAIL = "kris@gmail.com";
    private static final String UPDATE_USER_FIRST_NAME = "Kris";
    private static final String UPDATE_USER_LAST_NAME = "Fisher";
    private static final String UPDATE_USER_USER_NAME = "Kris";
    private static final String USER_PASSWORD =
            "$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG";
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Update user role")
    public void updateRole_WithUser_ReturnUserProfileResponseDto() {
        // Given
        UpdateRoleRequestDto roleRequestDto = new UpdateRoleRequestDto();
        roleRequestDto.setRoleName(Role.RoleName.USER);
        User john = new User();
        john.setId(USER_ID)
                .setUserName(USER_USER_NAME)
                .setPassword(USER_PASSWORD)
                .setEmail(USER_EMAIL)
                .setFirstName(USER_FIRST_NAME)
                .setLastName(USER_LAST_NAME);
        UserProfileResponseDto expectedProfileResponseDto = new UserProfileResponseDto()
                .setId(USER_ID)
                .setUserName(USER_USER_NAME)
                .setEmail(USER_EMAIL)
                .setFirstName(USER_FIRST_NAME)
                .setLastName(USER_LAST_NAME)
                .setRoleIds(Set.of(USER_ROLE_ID, ADMIN_ROLE_ID));

        // When
        when(userRepository.findUserById(john.getId()))
                .thenReturn(Optional.of(john));
        when(roleRepository.findRoleByRoleName(roleRequestDto.getRoleName()))
                .thenReturn(Optional.of(new Role().setId(ADMIN_ROLE_ID)));
        when(userRepository.save(john)).thenReturn(john);
        when(userMapper.toResponseDtoWithRoles(john)).thenReturn(expectedProfileResponseDto);

        // Then
        UserProfileResponseDto actualProfileResponseDto =
                userService.updateRole(john.getId(), roleRequestDto);
        assertNotNull(actualProfileResponseDto);
        assertEquals(expectedProfileResponseDto, actualProfileResponseDto);
    }

    @Test
    @DisplayName("Get user profile info")
    public void getProfile_WithUser_ReturnUserProfileResponseDto() {
        // Given
        User john = new User();
        john.setId(USER_ID)
                .setUserName(USER_USER_NAME)
                .setPassword(USER_PASSWORD)
                .setEmail(USER_EMAIL)
                .setFirstName(USER_FIRST_NAME)
                .setLastName(USER_LAST_NAME);
        UserProfileResponseDto expectedProfileResponseDto = new UserProfileResponseDto()
                .setId(USER_ID)
                .setUserName(USER_USER_NAME)
                .setEmail(USER_EMAIL)
                .setFirstName(USER_FIRST_NAME)
                .setLastName(USER_LAST_NAME)
                .setRoleIds(Set.of(USER_ROLE_ID));

        // When
        when(userRepository.findUserById(john.getId()))
                .thenReturn(Optional.of(john));
        when(userMapper.toResponseDtoWithRoles(john)).thenReturn(expectedProfileResponseDto);

        // Then
        UserProfileResponseDto actualProfileResponseDto =
                userService.getProfile(john.getId());
        assertNotNull(actualProfileResponseDto);
        assertEquals(expectedProfileResponseDto, actualProfileResponseDto);
    }

    @Test
    @DisplayName("Update user profile info")
    public void updateProfileInfo_WithUser_ReturnUserProfileResponseDto() {
        // Given
        UpdateProfileRequestDto profileRequestDto = new UpdateProfileRequestDto();
        profileRequestDto
                .setUserName(UPDATE_USER_USER_NAME)
                .setEmail(UPDATE_USER_EMAIL)
                .setFirstName(UPDATE_USER_FIRST_NAME)
                .setLastName(UPDATE_USER_LAST_NAME);
        User john = new User();
        john.setId(USER_ID)
                .setUserName(USER_USER_NAME)
                .setPassword(USER_PASSWORD)
                .setEmail(USER_EMAIL)
                .setFirstName(USER_FIRST_NAME)
                .setLastName(USER_LAST_NAME)
                .setRoleSet(Set.of(new Role().setId(USER_ROLE_ID)));
        User kris = new User();
        kris.setId(USER_ID)
                .setUserName(UPDATE_USER_USER_NAME)
                .setEmail(UPDATE_USER_EMAIL)
                .setFirstName(UPDATE_USER_FIRST_NAME)
                .setLastName(UPDATE_USER_LAST_NAME);
        UserProfileResponseDto expectedProfileResponseDto = new UserProfileResponseDto()
                .setId(USER_ID)
                .setUserName(UPDATE_USER_USER_NAME)
                .setEmail(UPDATE_USER_EMAIL)
                .setFirstName(UPDATE_USER_FIRST_NAME)
                .setLastName(UPDATE_USER_LAST_NAME)
                .setRoleIds(Set.of(USER_ROLE_ID));

        // When
        when(userRepository.findUserById(john.getId()))
                .thenReturn(Optional.of(john));
        when(userMapper.toEntityFromUpdateRequest(profileRequestDto)).thenReturn(kris);
        when(userRepository.save(kris)).thenReturn(kris);
        when(userMapper.toResponseDtoWithRoles(kris)).thenReturn(expectedProfileResponseDto);

        // Then
        UserProfileResponseDto actualProfileResponseDto =
                userService.updateProfileInfo(john.getId(), profileRequestDto);
        assertNotNull(actualProfileResponseDto);
        assertEquals(expectedProfileResponseDto, actualProfileResponseDto);
    }
}
