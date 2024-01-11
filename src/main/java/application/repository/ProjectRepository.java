package application.repository;

import application.model.Project;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsProjectById(Long id);

    @Query("FROM Project ")
    List<Project> getAllProjects(Pageable pageable);
}
