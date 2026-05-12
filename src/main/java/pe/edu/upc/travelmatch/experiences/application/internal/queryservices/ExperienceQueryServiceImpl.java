package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllExperiencesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetExperienceByIdQuery;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

/**
 * Service implementation for managing Experience queries.
 */
@Service
public class ExperienceQueryServiceImpl implements ExperienceQueryService {

  private final ExperienceRepository experienceRepository;

  /**
   * Constructs an ExperienceQueryServiceImpl.
   *
   * @param experienceRepository the experience repository
   */
  public ExperienceQueryServiceImpl(ExperienceRepository experienceRepository) {
    this.experienceRepository = experienceRepository;
  }

  @Override
  public List<Experience> handle(GetAllExperiencesQuery query) {
    return experienceRepository.findAll();
  }

  @Override
  public Optional<Experience> handle(GetExperienceByIdQuery query) {
    return experienceRepository.findById(query.experienceId());
  }
}