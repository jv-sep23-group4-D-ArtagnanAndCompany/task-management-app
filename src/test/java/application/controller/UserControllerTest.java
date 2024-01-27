package application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.user.UpdateProfileRequestDto;
import application.dto.user.UpdateRoleRequestDto;
import application.dto.user.UserProfileResponseDto;
import application.mapper.UserMapper;
import application.model.Role;
import application.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    protected static MockMvc mockMvc;
    private static final Long ADMIN_ROLE_ID = 1L;
    private static final Long USER_ROLE_ID = 2L;
    private static final Long USER1_ID = 3L;
    private static final String USER1_EMAIL = "john1@gmail.com";
    private static final String USER1_FIRST_NAME = "John";
    private static final String USER1_LAST_NAME = "Lollipop";
    private static final String USER1_USER_NAME = "John";
    private static final String USER3_EMAIL = "john12@gmail.com";
    private static final String USER3_FIRST_NAME = "John12";
    private static final String USER3_LAST_NAME = "Lollipop12";
    private static final String USER3_USER_NAME = "John12";
    private static final String USER_PASSWORD =
            "$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG";
    private static final String USER2_EMAIL = "kris1@gmail.com";
    private static final String USER2_FIRST_NAME = "Kris";
    private static final String USER2_LAST_NAME = "Fisher";
    private static final String USER2_USER_NAME = "Kris";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    @SneakyThrows
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        User john = new User();
        john.setId(USER1_ID);
        john.setEmail(USER1_EMAIL);
        john.setPassword(USER_PASSWORD);
        Role roleAdmin = new Role().setId(ADMIN_ROLE_ID).setRoleName(Role.RoleName.ADMIN);
        Role roleUser = new Role().setId(USER_ROLE_ID).setRoleName(Role.RoleName.USER);
        john.setRoleSet(Set.of(roleAdmin, roleUser));
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(john,
                john.getPassword(), john.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @Sql(scripts = "classpath:database/users/add_two_default_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/users/remove_two_added_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Update user role")
    public void updateUserRole_WithAdminAndUser_ReturnProfileInfoWithUpdatedRole()
            throws Exception {
        // Given
        User user = new User();
        user.setId(4L);
        user.setUserName(USER2_USER_NAME);
        user.setPassword(USER_PASSWORD);
        user.setEmail(USER2_EMAIL);
        user.setFirstName(USER2_FIRST_NAME);
        user.setLastName(USER2_LAST_NAME);
        Role roleUser = new Role().setId(USER_ROLE_ID);
        Role roleAdmin = new Role().setId(ADMIN_ROLE_ID);
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        roles.add(roleAdmin);
        user.setRoleSet(roles);
        UpdateRoleRequestDto roleRequestDto = new UpdateRoleRequestDto();
        roleRequestDto.setRoleName(Role.RoleName.ADMIN);
        String jsonBody = objectMapper.writeValueAsString(roleRequestDto);

        // When
        MvcResult result = mockMvc.perform(put("/api/users/{id}/role", 3)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserProfileResponseDto userProfileResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertEquals(userMapper.toSetIds(roles), userProfileResponseDto.getRoleIds());
    }

    @Test
    @Sql(scripts = "classpath:database/users/add_two_default_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/users/remove_two_added_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get user profile info")
    public void getProfileInfo_WithUser_ReturnProfileInfo() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        UserProfileResponseDto userProfileResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserProfileResponseDto.class
        );
        assertEquals(userProfileResponseDto.getId(), USER1_ID);
        assertEquals(userProfileResponseDto.getUserName(), USER1_USER_NAME);
        assertEquals(userProfileResponseDto.getEmail(), USER1_EMAIL);
        assertEquals(userProfileResponseDto.getFirstName(), USER1_FIRST_NAME);
        assertEquals(userProfileResponseDto.getLastName(), USER1_LAST_NAME);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Update user role")
    @Sql(scripts = "classpath:database/users/add_two_default_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/users/remove_two_added_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateProfileInfo_WithUser_ReturnProfileInfo() throws Exception {
        // Given
        UpdateProfileRequestDto profileRequestDto = new UpdateProfileRequestDto();
        profileRequestDto.setUserName(USER3_USER_NAME);
        profileRequestDto.setEmail(USER3_EMAIL);
        profileRequestDto.setFirstName(USER3_FIRST_NAME);
        profileRequestDto.setLastName(USER3_LAST_NAME);
        String jsonBody = objectMapper.writeValueAsString(profileRequestDto);
        // When
        MvcResult result = mockMvc.perform(put("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isOk()).andReturn();
        // Then
        Role roleUser = new Role().setId(USER_ROLE_ID);
        Role roleAdmin = new Role().setId(ADMIN_ROLE_ID);
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        roles.add(roleAdmin);
        UserProfileResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                UserProfileResponseDto.class
        );
        UserProfileResponseDto expected = new UserProfileResponseDto();
        expected.setId(USER1_ID).setUserName(profileRequestDto.getUserName())
                .setEmail(profileRequestDto.getEmail())
                .setFirstName(profileRequestDto.getFirstName())
                .setLastName(profileRequestDto.getLastName());
        expected.setRoleIds(userMapper.toSetIds(roles));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
