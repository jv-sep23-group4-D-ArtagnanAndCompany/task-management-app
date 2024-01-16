package application.repository;

import application.model.Task;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> getAllByProjectId(Long projectId);

    @EntityGraph(attributePaths = "assignee")
    List<Task> getAllByDueDateBetweenAndStatusIsNot(LocalDate from, LocalDate to,
                                                    Task.Status status);
}
