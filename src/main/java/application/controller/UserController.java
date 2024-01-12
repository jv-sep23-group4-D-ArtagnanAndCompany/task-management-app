package application.controller;

import application.dto.user.UpdateProfileRequestDto;
import application.dto.user.UpdateRoleRequestDto;
import application.dto.user.UserResponseDto;
import application.model.User;
import application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Managing authentication and user registration",
        description = "Managing authentication and user registration")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role",
            description = "Update user role by user id")
    UserResponseDto updateUserRole(@PathVariable Long id,
                                   @RequestBody UpdateRoleRequestDto roleRequestDto
    ) {
        return userService.updateRole(id, roleRequestDto);
    }

    @GetMapping("/me")
    @Operation(summary = "Get user profile info",
            description = "Get user profile info")
    UserResponseDto getProfileInfo(Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        return userService.getProfile(user.getId());
    }

    @PutMapping("/me")
    @Operation(summary = "Update user profile info",
            description = "Update user profile info")
    UserResponseDto updateProfileInfo(Authentication authentication,
                                      UpdateProfileRequestDto profileRequestDto) {
        User user = (User)authentication.getPrincipal();
        return userService.updateProfileInfo(user.getId(), profileRequestDto);
    }
}
