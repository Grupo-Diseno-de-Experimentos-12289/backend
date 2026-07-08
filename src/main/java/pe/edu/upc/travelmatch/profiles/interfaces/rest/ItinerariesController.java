package pe.edu.upc.travelmatch.profiles.interfaces.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.profiles.domain.services.ItineraryQueryService;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.GenerateItineraryResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.ItineraryResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.GenerateItineraryQueryFromResourceAssembler;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.ItineraryResourceFromEntityAssembler;

/** REST controller for personalized itinerary generation endpoints. */
@RestController
@RequestMapping(value = "/api/v1/itineraries", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Itineraries", description = "Personalized Itinerary Generation Endpoints")
public class ItinerariesController {

  private final ItineraryQueryService itineraryQueryService;

  /**
   * Constructs a new ItinerariesController.
   *
   * @param itineraryQueryService the service to generate itineraries
   */
  public ItinerariesController(ItineraryQueryService itineraryQueryService) {
    this.itineraryQueryService = itineraryQueryService;
  }

  /**
   * Generates a personalized itinerary based on destination, categories and number of days.
   *
   * @param resource the request body containing destination, categories and days
   * @return the generated itinerary resource or 400 if data is insufficient
   */
  @PostMapping("/generate")
  public ResponseEntity<ItineraryResource> generateItinerary(
      @RequestBody GenerateItineraryResource resource) {
    try {
      var query = GenerateItineraryQueryFromResourceAssembler.toQueryFromResource(resource);
      var itinerary = itineraryQueryService.handle(query);
      var itineraryResource = ItineraryResourceFromEntityAssembler.toResourceFromEntity(itinerary);
      return ResponseEntity.ok(itineraryResource);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }
}