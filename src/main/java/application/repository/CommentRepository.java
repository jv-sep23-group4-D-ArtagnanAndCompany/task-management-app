package application.repository;

import application.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"task", "user"})
    List<Comment> findAllByTaskIdAndUserId(Long taskId, Long userId);
}
