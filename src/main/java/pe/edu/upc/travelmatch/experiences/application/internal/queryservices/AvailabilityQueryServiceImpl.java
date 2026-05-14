package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAvailabilityByIdQuery;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;

/** Service implementation for managing Availability queries. */
@Service
public class AvailabilityQueryServiceImpl implements AvailabilityQueryService {

  private final AvailabilityRepository availabilityRepository;

  /**
   * Constructs an AvailabilityQueryServiceImpl.
   *
   * @param availabilityRepository the availability repository
   */
  public AvailabilityQueryServiceImpl(AvailabilityRepository availabilityRepository) {
    this.availabilityRepository = availabilityRepository;
  }

  @Override
  public List<Availability> getAllAvailabilities() {
    return availabilityRepository.findAllByDeletedAtIsNull();
  }

  @Override
  public Optional<Availability> handle(GetAvailabilityByIdQuery query) {
    return availabilityRepository.findById(query.availabilityId());
  }
}
