package application.dto.project;

import application.model.Project;
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
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private Project.Status status;
}
