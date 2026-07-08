package pe.edu.upc.travelmatch.experiences.domain.model.queries;

/**
 * Query to get all availabilities of a given experience.
 *
 * @param experienceId the experience ID
 */
public record GetAvailabilitiesByExperienceIdQuery(Long experienceId) {}
