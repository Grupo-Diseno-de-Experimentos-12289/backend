package pe.edu.upc.travelmatch.experiences.domain.model.commands;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;

public record CreateExperienceMediaCommand(
        Experience experience,
        String mediaUrl,
        String caption
) {}