package pe.edu.upc.travelmatch.experiences.domain.model.commands;
import java.time.LocalDateTime;
/**
 * Command to update an availability.
 *
 * @param availabilityId the availability ID
 * @param startDateTime  the start date and time
 * @param endDateTime    the end date and time
 * @param capacity       the capacity
 */
public record UpdateAvailabilityCommand(
    Long availabilityId,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    int capacity) {
}
