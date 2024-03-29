package application.repository;

import application.model.Attachment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("SELECT a FROM Attachment a JOIN FETCH a.task t WHERE t.id = :taskId")
    List<Attachment> findAllByTaskId(Long taskId);
}
