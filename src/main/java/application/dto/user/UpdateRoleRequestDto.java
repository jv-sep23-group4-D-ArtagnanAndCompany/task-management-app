package application.dto.user;

import application.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequestDto {
    @NotNull(message = " can't be empty")
    private Role.RoleName roleName;
}
