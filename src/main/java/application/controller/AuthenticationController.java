package application.controller;

import application.dto.user.UserRequestLoginDto;
import application.dto.user.UserRequestRegistrationDto;
import application.dto.user.UserResponseDto;
import application.dto.user.UserResponseLoginDto;
import application.security.AuthenticationService;
import application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication management", description = "Endpoints for user login and registration")
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user", description = "Endpoint for user registration")
    public UserResponseDto register(@Valid @RequestBody
                                        UserRequestRegistrationDto userRequestDto) {
        return userService.register(userRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login user", description = "Endpoint for user login")
    public UserResponseLoginDto login(@Valid @RequestBody
                                      UserRequestLoginDto userRequestLoginDto) {
        return authenticationService.login(userRequestLoginDto);
    }
}
