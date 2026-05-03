package pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import java.util.List;

@Repository
public interface ExperienceMediaRepository extends JpaRepository<ExperienceMedia, Long> {
    List<ExperienceMedia> findByExperienceId(Long experienceId);
}
