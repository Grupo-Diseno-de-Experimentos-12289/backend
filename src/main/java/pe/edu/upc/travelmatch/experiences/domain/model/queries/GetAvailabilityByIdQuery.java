package pe.edu.upc.travelmatch.experiences.domain.model.queries;

/**
 * Query to get an availability by its ID.
 *
 * @param availabilityId the availability ID
 */
public record GetAvailabilityByIdQuery(Long availabilityId) {}
