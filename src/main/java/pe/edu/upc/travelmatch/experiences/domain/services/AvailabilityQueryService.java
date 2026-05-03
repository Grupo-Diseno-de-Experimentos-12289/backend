package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAvailabilityByIdQuery;

import java.util.List;
import java.util.Optional;

public interface AvailabilityQueryService {
    List<Availability> getAllAvailabilities();
    Optional<Availability> handle(GetAvailabilityByIdQuery query);
}