package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetCorporateRecommendationsQuery;
import pe.edu.upc.travelmatch.experiences.domain.services.RecommendationQueryService;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CorporateRecommendationResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.transform.CorporateRecommendationResourceFromResultAssembler;

import java.time.LocalDateTime;
import java.util.List;

/** RecommendationsController. */
@RestController
@RequestMapping(value = "/api/v1/experiences/recommendations")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:4200"},
    methods = {RequestMethod.GET})
@Tag(name = "Recommendations", description = "Personalized Recommendation Endpoints")
public class RecommendationsController {

  private final RecommendationQueryService recommendationQueryService;

  /** Constructs a new RecommendationsController. */
  public RecommendationsController(RecommendationQueryService recommendationQueryService) {
    this.recommendationQueryService = recommendationQueryService;
  }

  /** Documentation. */
  @Operation(
      summary = "Get personalized recommendations for a corporate traveler",
      description =
          "Recommends experiences based on the traveler's current location (destination),"
              + " interests (categories) and the free time window available in their work"
              + " schedule. Only returns experiences with at least one in-stock availability slot"
              + " that fits entirely inside the given time window, ordered by the soonest slot"
              + " first so the traveler can optimize their agenda.",
      responses = {
        @ApiResponse(responseCode = "200", description = "List of recommendations returned"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
      })
  @GetMapping("/corporate")
  public ResponseEntity<List<CorporateRecommendationResource>> getCorporateRecommendations(
      @RequestParam Long destinationId,
      @RequestParam(required = false) List<String> interests,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime windowStart,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime windowEnd) {
    var query = new GetCorporateRecommendationsQuery(destinationId, interests, windowStart, windowEnd);
    var result = recommendationQueryService.handle(query);
    var resources =
        result.stream()
            .map(CorporateRecommendationResourceFromResultAssembler::toResourceFromResult)
            .toList();

    return ResponseEntity.ok(resources);
  }
}
