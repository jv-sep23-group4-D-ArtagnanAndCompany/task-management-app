package application.dto.task;

import application.dto.ValidationMessages;
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
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String name;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String description;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String priority;
    @NotBlank(message = ValidationMessages.NOT_NULL)
    private String status;
    @NotNull(message = ValidationMessages.NOT_NULL)
    private LocalDate dueDate;
    @NotNull
    @Positive(message = ValidationMessages.POSITIVE)
    private Long projectId;
    @NotNull
    @Positive(message = ValidationMessages.POSITIVE)
    private Long assigneeId;
}
