package application.repository;

import application.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
        FROM Comment c
        JOIN FETCH c.task t
        JOIN FETCH c.user u
        WHERE t.id =:taskId
        AND u.id =:userId
            """)
    List<Comment> findAllByTaskId(Long taskId, Long userId);
}
