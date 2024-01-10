package application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE tasks SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Data
@Accessors(chain = true)
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @Enumerated
    @Column(nullable = false, columnDefinition = "varchar")
    private Priority priority;
    @Enumerated
    @Column(nullable = false, columnDefinition = "varchar")
    private Status status;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private Long projectId;
    @NotNull
    private Long assigneeId;
    @NotNull
    private boolean isDeleted = false;

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }
}
