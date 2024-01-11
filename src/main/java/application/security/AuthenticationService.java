package application.security;

import application.dto.user.UserRequestLoginDto;
import application.dto.user.UserResponseLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    public UserResponseLoginDto login(UserRequestLoginDto userRequestLoginDto) {
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userRequestLoginDto.getEmail(),
                        userRequestLoginDto.getPassword()));
        String token = jwtUtil.generateToken(userRequestLoginDto.getEmail());
        return new UserResponseLoginDto(token);
    }
}
