package pe.edu.upc.travelmatch.experiences.domain.model.commands;


public record UpdateExperienceMediaCommand(
        Long id,
        String mediaUrl,
        String caption
) {}