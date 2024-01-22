package application.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class TaskRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String priority;
    @NotBlank
    private String status;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    @Positive(message = " cannot be less then 1")
    private Long projectId;
    @NotNull
    @Positive(message = " cannot be less then 1")
    private Long assigneeId;
}
