package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.AvailabilityTicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.CorporateRecommendation;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetCorporateRecommendationsQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.services.RecommendationQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Service implementation for personalized corporate-traveler recommendations. Matches experiences
 * by destination (location) and category (interests), then keeps only the availability slots that
 * fit inside the traveler's free time window and still have stock (schedule fit), so results can
 * be booked without disrupting the traveler's agenda.
 */
@Service
public class RecommendationQueryServiceImpl implements RecommendationQueryService {

  private final ExperienceRepository experienceRepository;
  private final AvailabilityRepository availabilityRepository;

  /**
   * Constructs a RecommendationQueryServiceImpl.
   *
   * @param experienceRepository the experience repository
   * @param availabilityRepository the availability repository
   */
  public RecommendationQueryServiceImpl(
      ExperienceRepository experienceRepository, AvailabilityRepository availabilityRepository) {
    this.experienceRepository = experienceRepository;
    this.availabilityRepository = availabilityRepository;
  }

  @Override
  public List<CorporateRecommendation> handle(GetCorporateRecommendationsQuery query) {
    if (query.destinationId() == null) {
      throw new IllegalArgumentException("destinationId is required to compute recommendations.");
    }
    if (query.windowStart() == null || query.windowEnd() == null) {
      throw new IllegalArgumentException("windowStart and windowEnd are required.");
    }
    if (!query.windowStart().isBefore(query.windowEnd())) {
      throw new IllegalArgumentException("windowStart must be before windowEnd.");
    }

    List<Categories> categories = resolveCategories(query.interests());

    List<Experience> candidates =
        experienceRepository.findAllByDestinationId_ValueAndCategory_NameInAndDeletedAtIsNull(
            query.destinationId(), categories);

    return candidates.stream()
        .map(experience -> toRecommendation(experience, query))
        .filter(recommendation -> !recommendation.matchingAvailabilities().isEmpty())
        .sorted(
            Comparator.comparing(
                recommendation -> recommendation.matchingAvailabilities().get(0).getStartDateTime()))
        .toList();
  }

  private List<Categories> resolveCategories(List<String> interests) {
    if (interests == null || interests.isEmpty()) {
      return List.of(Categories.values());
    }
    return interests.stream()
        .filter(interest -> interest != null && !interest.isBlank())
        .map(String::trim)
        .map(String::toUpperCase)
        .map(
            name -> {
              try {
                return Categories.valueOf(name);
              } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unknown interest/category: " + name, ex);
              }
            })
        .distinct()
        .toList();
  }

  private CorporateRecommendation toRecommendation(
      Experience experience, GetCorporateRecommendationsQuery query) {
    var matchingAvailabilities =
        availabilityRepository
            .findAllByExperience_IdAndDeletedAtIsNullOrderByStartDateTimeAsc(experience.getId())
            .stream()
            .filter(availability -> fitsInWindow(availability, query.windowStart(), query.windowEnd()))
            .filter(this::hasAvailableStock)
            .sorted(Comparator.comparing(Availability::getStartDateTime))
            .toList();

    return new CorporateRecommendation(experience, matchingAvailabilities);
  }

  private boolean fitsInWindow(
      Availability availability, LocalDateTime windowStart, LocalDateTime windowEnd) {
    return !availability.getStartDateTime().isBefore(windowStart)
        && !availability.getEndDateTime().isAfter(windowEnd);
  }

  private boolean hasAvailableStock(Availability availability) {
    return availability.getTicketTypes().stream()
        .mapToInt(AvailabilityTicketType::getStock)
        .sum()
        > 0;
  }
}
