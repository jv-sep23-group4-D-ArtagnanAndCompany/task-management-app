package application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
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

    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Column(nullable = false, columnDefinition = "varchar")
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Column(nullable = false, columnDefinition = "varchar")
    private Status status;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate dueDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "project_id")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "assignee_id")
    private User assignee;
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
