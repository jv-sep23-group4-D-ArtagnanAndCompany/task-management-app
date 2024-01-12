package application.dto.user;

import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserProfileResponseDto {
    private Long id;
    private String email;
    private String userName;
    private String firstName;
    private String lastName;
    private Set<Long> roleIds;
}
