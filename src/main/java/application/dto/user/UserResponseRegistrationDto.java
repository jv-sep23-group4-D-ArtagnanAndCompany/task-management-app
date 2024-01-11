package application.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResponseRegistrationDto {
    private Long id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
}
