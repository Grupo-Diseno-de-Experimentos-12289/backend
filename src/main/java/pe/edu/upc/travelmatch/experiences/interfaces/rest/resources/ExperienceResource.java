package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;

public record ExperienceResource(
        Long id,
        String title,
        String description,
        Long agencyId,
        Category category,
        Long destinationId,
        String duration,
        String meetingPoint
) {}