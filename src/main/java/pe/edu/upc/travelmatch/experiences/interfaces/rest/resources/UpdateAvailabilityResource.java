package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UpdateAvailabilityResource(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        int capacity
) {}
