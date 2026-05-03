package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceCommand;

public interface ExperienceCommandService {
    Long handle(CreateExperienceCommand command);
    void deleteExperience(Long experienceId);
    void updateExperience(UpdateExperienceCommand command);
}