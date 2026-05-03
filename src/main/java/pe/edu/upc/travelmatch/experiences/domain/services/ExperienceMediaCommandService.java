package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;

import java.util.Optional;

public interface ExperienceMediaCommandService {
    Long handle(CreateExperienceMediaCommand command);
    Optional<ExperienceMedia> handle(UpdateExperienceMediaCommand command);
    void deleteById(Long id);
}