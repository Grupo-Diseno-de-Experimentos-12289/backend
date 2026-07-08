package pe.edu.upc.travelmatch.experiences.domain.model.queries;

public record GetAvailabilitiesByExperienceIdQuery(
        Long experienceId
) {
    public GetAvailabilitiesByExperienceIdQuery {
        if (experienceId == null || experienceId <= 0) {
            throw new IllegalArgumentException("experienceId must not be null or less than or equal to zero.");
        }
    }
}
