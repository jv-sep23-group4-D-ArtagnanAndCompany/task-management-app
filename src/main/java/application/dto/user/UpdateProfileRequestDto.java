package application.dto.user;

import application.dto.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateProfileRequestDto {
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String userName;
    @Email(message = ValidationMessages.EMAIL)
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String email;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String firstName;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String lastName;
}
