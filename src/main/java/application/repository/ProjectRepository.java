package application.repository;

import application.model.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsProjectById(Long id);

    List<Project> findAllByUserId(Long userId, Pageable pageable);

    @Query("FROM Project p JOIN FETCH p.user u WHERE p.id =:id AND u.id =:userId")
    Optional<Project> findById(Long userId, Long id);
}
