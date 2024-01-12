package application.dto.task;

import java.time.LocalDate;
import lombok.Data;

@Data
public class TaskResponseDto {

    private Long id;

    private String name;

    private String description;

    private String priority;

    private String status;

    private LocalDate dueDate;

    private Long projectId;

    private Long assigneeId;

}
