package pe.edu.upc.travelmatch.experiences.domain.model.commands;

import java.time.LocalDateTime;
public record UpdateAvailabilityCommand(
        Long availabilityId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        int capacity
) {}