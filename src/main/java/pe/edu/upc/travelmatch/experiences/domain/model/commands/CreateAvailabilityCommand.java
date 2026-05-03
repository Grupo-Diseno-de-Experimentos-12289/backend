package pe.edu.upc.travelmatch.experiences.domain.model.commands;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;

import java.time.LocalDateTime;

public record CreateAvailabilityCommand(
        Experience experience,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        int capacity
) {}