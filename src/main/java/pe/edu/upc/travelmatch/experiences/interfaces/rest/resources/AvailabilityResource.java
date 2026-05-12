package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * AvailabilityResource(.
 */
public record AvailabilityResource(
    Long id,
    Long experienceId,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    int capacity
) {}
