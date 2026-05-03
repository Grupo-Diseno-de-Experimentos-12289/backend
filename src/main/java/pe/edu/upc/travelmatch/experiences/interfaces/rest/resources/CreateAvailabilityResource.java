package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import java.time.LocalDateTime;

public record CreateAvailabilityResource(
        Long experienceId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        int capacity
) {}