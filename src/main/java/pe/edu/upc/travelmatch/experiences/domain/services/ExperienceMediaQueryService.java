package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;

import java.util.List;
import java.util.Optional;

public interface ExperienceMediaQueryService {
    List<ExperienceMedia> getAll();
    List<ExperienceMedia> findByExperienceId(Long experienceId);
    Optional<ExperienceMedia> findById(Long id);
}
