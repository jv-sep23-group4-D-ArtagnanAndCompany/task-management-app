package application.dto.user;

import application.dto.ValidationMessages;
import application.validation.PasswordsAreEqual;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@PasswordsAreEqual
public class UserRequestRegistrationDto {
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String userName;
    @Size(min = 8, message = ValidationMessages.PASSWORD_LONGER)
    @Size(max = 40, message = ValidationMessages.PASSWORD_SHORTER)
    private String password;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String repeatPassword;
    @NotBlank
    @Email(message = ValidationMessages.EMAIL)
    private String email;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String firstName;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String lastName;
}
