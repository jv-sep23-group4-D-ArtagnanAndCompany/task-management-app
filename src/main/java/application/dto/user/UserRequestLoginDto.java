package application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRequestLoginDto {
    @Email(message = "Email is incorrect")
    private String email;
    @Size(min = 8, message = "Password must be longer")
    private String password;
}
