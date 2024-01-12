package application.repository;

import application.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t JOIN FETCH t.projectId")
    List<Task> getAllByProjectId(Long projectId);
}
