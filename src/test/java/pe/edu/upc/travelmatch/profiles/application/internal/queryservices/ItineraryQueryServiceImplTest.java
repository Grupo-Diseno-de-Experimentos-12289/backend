package pe.edu.upc.travelmatch.profiles.application.internal.queryservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.dto.ExperienceSummary;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GenerateItineraryQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Itinerary;

@ExtendWith(MockitoExtension.class)
class ItineraryQueryServiceImplTest {

  @Mock
  private ExternalExperienceService externalExperienceService;

  @InjectMocks
  private ItineraryQueryServiceImpl itineraryQueryService;

  @Test
  @DisplayName("handle() distributes experiences across days in round-robin order")
  void handle_multipleExperiences_distributesActivitiesAcrossDays() {
    // Arrange
    var query = new GenerateItineraryQuery(1L, List.of("CULTURA"), 2);
    var experiences =
        List.of(
            new ExperienceSummary(1L, "Tour Centro Historico", "CULTURA", "Plaza Mayor", "3h", 1L),
            new ExperienceSummary(2L, "Museo Larco", "CULTURA", "Museo", "2h", 1L),
            new ExperienceSummary(3L, "Tour Gastronomico", "CULTURA", "Mercado", "4h", 1L));
    when(externalExperienceService.fetchExperiencesByDestinationAndCategories(
        1L, List.of("CULTURA")))
        .thenReturn(experiences);

    // Act
    Itinerary itinerary = itineraryQueryService.handle(query);

    // Assert
    assertEquals(1L, itinerary.destinationId());
    assertEquals(2, itinerary.numberOfDays());
    assertEquals(2, itinerary.days().size());
    assertEquals(2, itinerary.days().get(0).activities().size());
    assertEquals(1, itinerary.days().get(1).activities().size());
    assertEquals(1L, itinerary.days().get(0).activities().get(0).experienceId());
    assertEquals(3L, itinerary.days().get(0).activities().get(1).experienceId());
    assertEquals(2L, itinerary.days().get(1).activities().get(0).experienceId());
  }

  @Test
  @DisplayName("handle() throws IllegalStateException when no experiences match the preferences")
  void handle_noExperiencesFound_throwsIllegalState() {
    // Arrange
    var query = new GenerateItineraryQuery(99L, List.of("AVENTURA"), 3);
    when(externalExperienceService.fetchExperiencesByDestinationAndCategories(
        99L, List.of("AVENTURA")))
        .thenReturn(List.of());

    // Act & Assert
    IllegalStateException exception =
        assertThrows(IllegalStateException.class, () -> itineraryQueryService.handle(query));

    assertTrue(exception.getMessage().contains("No experiences were found"));
  }

  @Test
  @DisplayName("handle() places every activity on day 1 when only a single day is requested")
  void handle_singleDay_allActivitiesOnDayOne() {
    // Arrange
    var query = new GenerateItineraryQuery(1L, List.of(), 1);
    var experiences =
        List.of(
            new ExperienceSummary(1L, "Tour A", "AVENTURA", "Punto A", "1h", 1L),
            new ExperienceSummary(2L, "Tour B", "AVENTURA", "Punto B", "1h", 1L));
    when(externalExperienceService.fetchExperiencesByDestinationAndCategories(1L, List.of()))
        .thenReturn(experiences);

    // Act
    Itinerary itinerary = itineraryQueryService.handle(query);

    // Assert
    assertEquals(1, itinerary.days().size());
    assertEquals(2, itinerary.days().get(0).activities().size());
  }

  @Test
  @DisplayName("handle() creates empty day lists when there are more days than experiences")
  void handle_moreDaysThanExperiences_someDaysRemainEmpty() {
    // Arrange
    var query = new GenerateItineraryQuery(1L, List.of("CULTURA"), 3);
    var experiences =
        List.of(new ExperienceSummary(1L, "Tour Unico", "CULTURA", "Plaza", "2h", 1L));
    when(externalExperienceService.fetchExperiencesByDestinationAndCategories(
        1L, List.of("CULTURA")))
        .thenReturn(experiences);

    // Act
    Itinerary itinerary = itineraryQueryService.handle(query);

    // Assert
    assertEquals(3, itinerary.days().size());
    assertEquals(1, itinerary.days().get(0).activities().size());
    assertTrue(itinerary.days().get(1).activities().isEmpty());
    assertTrue(itinerary.days().get(2).activities().isEmpty());
  }
}