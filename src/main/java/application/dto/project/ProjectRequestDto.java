package application.dto.project;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ProjectRequestDto {
    @NotBlank(message = "Name cannot be null or empty")
    private String name;
    @NotBlank(message = "Description cannot be null or empty")
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
