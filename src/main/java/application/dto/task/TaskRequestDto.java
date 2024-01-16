package application.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Long projectId;
    @NotNull
    private Long assigneeId;
}
