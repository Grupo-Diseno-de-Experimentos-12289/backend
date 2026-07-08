package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.CorporateRecommendation;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetCorporateRecommendationsQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.domain.services.RecommendationQueryService;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CorporateRecommendationResource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationsControllerTest {

  @Mock private RecommendationQueryService recommendationQueryService;

  @InjectMocks private RecommendationsController recommendationsController;

  @Test
  void testGetCorporateRecommendations_Ok() {
    // Arrange
    Experience experience =
        new Experience(
            "City Museum Guided Tour",
            "A short guided tour perfect for a lunch break",
            new AgencyId(1L),
            new Category(Categories.CULTURA),
            new DestinationId(100L),
            "1 hour",
            "Main Lobby",
            CancellationPolicyType.FLEXIBLE,
            "Free cancellation up to 24 hours before.");

    var recommendation = new CorporateRecommendation(experience, List.of());

    when(recommendationQueryService.handle(any(GetCorporateRecommendationsQuery.class)))
        .thenReturn(List.of(recommendation));

    // Act
    ResponseEntity<List<CorporateRecommendationResource>> response =
        recommendationsController.getCorporateRecommendations(
            100L,
            List.of("CULTURA"),
            LocalDateTime.of(2026, 7, 10, 12, 0),
            LocalDateTime.of(2026, 7, 10, 14, 0));

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("City Museum Guided Tour", response.getBody().get(0).title());
    assertEquals("CULTURA", response.getBody().get(0).category());
  }

  @Test
  void testGetCorporateRecommendations_EmptyResult() {
    // Arrange
    when(recommendationQueryService.handle(any(GetCorporateRecommendationsQuery.class)))
        .thenReturn(List.of());

    // Act
    ResponseEntity<List<CorporateRecommendationResource>> response =
        recommendationsController.getCorporateRecommendations(
            100L, null, LocalDateTime.of(2026, 7, 10, 12, 0), LocalDateTime.of(2026, 7, 10, 14, 0));

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
  }
}
