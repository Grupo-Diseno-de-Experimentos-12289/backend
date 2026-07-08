package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.CorporateRecommendation;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetCorporateRecommendationsQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.*;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecommendationQueryServiceImpl Tests")
class RecommendationQueryServiceImplTest {

  @Mock private ExperienceRepository experienceRepository;

  @Mock private AvailabilityRepository availabilityRepository;

  @InjectMocks private RecommendationQueryServiceImpl recommendationQueryService;

  private Experience experienceWithSlot(Long id) {
    return new Experience(
        "City Museum Guided Tour",
        "A short guided tour perfect for a lunch break",
        new AgencyId(1L),
        new Category(Categories.CULTURA),
        new DestinationId(100L),
        "1 hour",
        "Main Lobby",
        CancellationPolicyType.FLEXIBLE,
        "Free cancellation up to 24 hours before.") {
      @Override
      public Long getId() {
        return id;
      }
    };
  }

  private Availability availabilityWithStock(
      Experience experience, LocalDateTime start, LocalDateTime end, int stock) {
    Availability availability = new Availability(experience, start, end, 10);
    availability.addTicketType(
        new TicketType(TicketTypes.TICKET_GENERAL), BigDecimal.valueOf(50), stock);
    return availability;
  }

  @Test
  @DisplayName("Debe recomendar experiencias con disponibilidad dentro de la ventana horaria")
  void handle_returnsExperiencesWithSlotsInsideWindow() {
    // Arrange
    LocalDateTime windowStart = LocalDateTime.of(2026, 7, 10, 12, 0);
    LocalDateTime windowEnd = LocalDateTime.of(2026, 7, 10, 14, 0);

    Experience experience = experienceWithSlot(1L);
    Availability fittingSlot =
        availabilityWithStock(
            experience,
            LocalDateTime.of(2026, 7, 10, 12, 30),
            LocalDateTime.of(2026, 7, 10, 13, 30),
            5);
    Availability outsideSlot =
        availabilityWithStock(
            experience,
            LocalDateTime.of(2026, 7, 10, 16, 0),
            LocalDateTime.of(2026, 7, 10, 17, 0),
            5);

    when(experienceRepository.findAllByDestinationId_ValueAndCategory_NameInAndDeletedAtIsNull(
            100L, List.of(Categories.CULTURA)))
        .thenReturn(List.of(experience));
    when(availabilityRepository.findAllByExperience_IdAndDeletedAtIsNullOrderByStartDateTimeAsc(
            1L))
        .thenReturn(List.of(outsideSlot, fittingSlot));

    var query =
        new GetCorporateRecommendationsQuery(100L, List.of("CULTURA"), windowStart, windowEnd);

    // Act
    List<CorporateRecommendation> result = recommendationQueryService.handle(query);

    // Assert
    assertEquals(1, result.size());
    assertEquals(1, result.get(0).matchingAvailabilities().size());
    assertEquals(fittingSlot, result.get(0).matchingAvailabilities().get(0));
  }

  @Test
  @DisplayName("Debe excluir experiencias sin cupos disponibles (sin stock) en la ventana")
  void handle_excludesExperiencesWithoutStock() {
    // Arrange
    LocalDateTime windowStart = LocalDateTime.of(2026, 7, 10, 12, 0);
    LocalDateTime windowEnd = LocalDateTime.of(2026, 7, 10, 14, 0);

    Experience experience = experienceWithSlot(2L);
    Availability soldOutSlot =
        availabilityWithStock(
            experience,
            LocalDateTime.of(2026, 7, 10, 12, 30),
            LocalDateTime.of(2026, 7, 10, 13, 30),
            0);

    when(experienceRepository.findAllByDestinationId_ValueAndCategory_NameInAndDeletedAtIsNull(
            100L, List.of(Categories.values())))
        .thenReturn(List.of(experience));
    when(availabilityRepository.findAllByExperience_IdAndDeletedAtIsNullOrderByStartDateTimeAsc(
            2L))
        .thenReturn(List.of(soldOutSlot));

    var query = new GetCorporateRecommendationsQuery(100L, null, windowStart, windowEnd);

    // Act
    List<CorporateRecommendation> result = recommendationQueryService.handle(query);

    // Assert
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Debe lanzar excepcion si windowStart no es anterior a windowEnd")
  void handle_throwsWhenWindowIsInvalid() {
    // Arrange
    LocalDateTime sameInstant = LocalDateTime.of(2026, 7, 10, 12, 0);
    var query = new GetCorporateRecommendationsQuery(100L, null, sameInstant, sameInstant);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> recommendationQueryService.handle(query));
  }

  @Test
  @DisplayName("Debe lanzar excepcion si un interes no corresponde a una categoria valida")
  void handle_throwsWhenInterestIsUnknown() {
    // Arrange
    LocalDateTime windowStart = LocalDateTime.of(2026, 7, 10, 12, 0);
    LocalDateTime windowEnd = LocalDateTime.of(2026, 7, 10, 14, 0);
    var query =
        new GetCorporateRecommendationsQuery(
            100L, List.of("NOT_A_CATEGORY"), windowStart, windowEnd);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> recommendationQueryService.handle(query));
  }
}
