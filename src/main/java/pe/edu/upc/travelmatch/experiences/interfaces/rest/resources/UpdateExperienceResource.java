package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

public record UpdateExperienceResource(
        String title,
        String description,
        String category, // antes: Long categoryId
        Long destinationId,
        String duration,
        String meetingPoint
) {}