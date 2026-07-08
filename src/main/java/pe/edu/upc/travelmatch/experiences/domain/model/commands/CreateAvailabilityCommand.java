package pe.edu.upc.travelmatch.experiences.domain.model.commands;

import java.time.LocalDateTime;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;

/**
 * Command to create an availability.
 *
 * @param experience the experience
 * @param startDateTime the start date and time
 * @param endDateTime the end date and time
 * @param capacity the capacity
 */
public record CreateAvailabilityCommand(
    Experience experience, LocalDateTime startDateTime, LocalDateTime endDateTime, int capacity) {}
