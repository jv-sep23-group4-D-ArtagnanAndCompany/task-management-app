package application.dto.user;

import application.dto.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRequestLoginDto {
    @Email(message = ValidationMessages.EMAIL)
    private String email;
    @Size(min = 8, message = ValidationMessages.PASSWORD_LONGER)
    @Size(max = 40, message = ValidationMessages.PASSWORD_SHORTER)
    private String password;
}
