package pe.edu.upc.travelmatch.experiences.domain.model.commands;

import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.*;

public record CreateExperienceCommand(
        String title,
        String description,
        Long agencyId,
        String category,
        DestinationId destinationId,
        String duration,
        String meetingPoint
) {}
