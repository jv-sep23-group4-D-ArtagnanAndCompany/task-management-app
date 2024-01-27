package application.dto.user;

import application.dto.ValidationMessages;
import application.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequestDto {
    @NotNull(message = ValidationMessages.NOT_NULL)
    private Role.RoleName roleName;
}
