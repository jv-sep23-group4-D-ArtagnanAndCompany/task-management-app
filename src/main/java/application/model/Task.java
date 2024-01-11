package application.model;

import jakarta.persistence.*;

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
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Enumerated
    @Column(nullable = false, columnDefinition = "varchar")
    private Priority priority;
    @Enumerated
    @Column(nullable = false, columnDefinition = "varchar")
    private Status status;
    @Column(nullable = false)
    private LocalDate dueDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "assignee_id")
    private User assigneeId;
    @Column(nullable = false)
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
