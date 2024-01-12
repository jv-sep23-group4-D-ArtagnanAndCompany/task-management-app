package application.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ProjectUpdateDto {
    @NotBlank(message = "Name cannot be null or empty")
    private String name;
    @NotBlank(message = "Description cannot be null or empty")
    private String description;
    @NotNull(message = "Start date cannot be null or empty")
    private LocalDate startDate;
    @NotNull(message = "End date cannot be null or empty")
    private LocalDate endDate;
    @NotNull(message = "Status field cannot be null or empty")
    private String status;
}
