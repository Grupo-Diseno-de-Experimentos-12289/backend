package pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.ExperiencesContextFacade;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;

@Service("profileExternalExperienceService")
public class ExternalExperienceService {
    private final ExperiencesContextFacade experiencesContextFacade;

    public ExternalExperienceService(ExperiencesContextFacade experiencesContextFacade) {
        this.experiencesContextFacade = experiencesContextFacade;
    }

    public boolean existsExperienceById(ExperienceId experienceId) {
        return experiencesContextFacade.existsExperienceById(experienceId.experienceId());
    }
}
