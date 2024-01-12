package application.dto;

import application.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequestDto {
    @NotNull(message = "Can't be empty")
    private Role role;
}
