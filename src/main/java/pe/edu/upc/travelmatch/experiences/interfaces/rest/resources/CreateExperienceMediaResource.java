package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

public record CreateExperienceMediaResource(
        Long experienceId,
        String mediaUrl,
        String caption
) {}