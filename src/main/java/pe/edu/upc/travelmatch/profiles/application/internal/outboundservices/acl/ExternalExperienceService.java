package pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.ExperiencesContextFacade;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;

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
}
