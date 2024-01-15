package application.dto.task;

import java.time.LocalDate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
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
