package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllExperiencesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetExperienceByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ExperienceQueryService {
    List<Experience> handle(GetAllExperiencesQuery query);
    Optional<Experience> handle(GetExperienceByIdQuery query);
}