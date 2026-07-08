package pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.ExperiencesContextFacade;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import java.util.List;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.dto.ExperienceSummary;

/** ExternalExperienceService type. */
@Service("profileExternalExperienceService")
public class ExternalExperienceService {
  private final ExperiencesContextFacade experiencesContextFacade;

  /** Constructs a new ExternalExperienceService. */
  public ExternalExperienceService(ExperiencesContextFacade experiencesContextFacade) {
    this.experiencesContextFacade = experiencesContextFacade;
  }

  /** Exists experience by id. */
  public boolean existsExperienceById(ExperienceId experienceId) {
    return experiencesContextFacade.existsExperienceById(experienceId.experienceId());
  }
  /** Fetches experience summaries filtered by destination and categories. */
  public List<ExperienceSummary> fetchExperiencesByDestinationAndCategories(
          Long destinationId, List<String> categories) {
    return experiencesContextFacade.fetchExperiencesByDestinationAndCategories(
            destinationId, categories);
  }
}
