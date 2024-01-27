package application.dto.project;

import application.dto.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ProjectRequestDto {
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String name;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String description;
    @NotNull(message = ValidationMessages.NOT_NULL)
    private LocalDate startDate;
    @NotNull(message = ValidationMessages.NOT_NULL)
    private LocalDate endDate;
}
