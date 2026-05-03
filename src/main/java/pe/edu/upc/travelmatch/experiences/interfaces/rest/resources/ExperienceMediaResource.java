package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

public record ExperienceMediaResource(
        Long id,
        Long experienceId,
        String mediaUrl,
        String caption
) {}