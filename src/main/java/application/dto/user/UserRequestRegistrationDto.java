package application.dto.user;

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
    @NotBlank
    private String userName;
    @Size(min = 8, message = "must be longer")
    private String password;
    @NotBlank
    private String repeatedPassword;
    @Email(message = "is incorrect")
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
