package application.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ProjectRequestDto {
    @NotBlank(message = " cannot be null or empty")
    private String name;
    @NotBlank(message = " cannot be null or empty")
    private String description;
    @NotNull(message = " cannot be null or empty")
    private LocalDate startDate;
    @NotNull(message = " cannot be null or empty")
    private LocalDate endDate;
}
