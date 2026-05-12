package pe.edu.upc.travelmatch.experiences.domain.model.queries;
/**
 * Query to get an experience by its ID.
 *
 * @param experienceId the experience ID
 */
public record GetExperienceByIdQuery(Long experienceId) {
}
