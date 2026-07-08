package pe.edu.upc.travelmatch.profiles.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GenerateItineraryQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Itinerary;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ItineraryActivity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ItineraryDay;
import pe.edu.upc.travelmatch.profiles.domain.services.ItineraryQueryService;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.GenerateItineraryResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.ItineraryResource;

@ExtendWith(MockitoExtension.class)
class ItinerariesControllerTest {

    @Mock private ItineraryQueryService itineraryQueryService;

    @InjectMocks private ItinerariesController controller;

    @Test
    void generateItinerary_returnsOk_whenSuccess() {
        // Arrange
        var resource = new GenerateItineraryResource(1L, List.of("CULTURA"), 1);
        var activity = new ItineraryActivity(1L, "Tour Centro", "CULTURA", "Plaza Mayor", "3h");
        var itinerary = new Itinerary(1L, 1, List.of(new ItineraryDay(1, List.of(activity))));
        when(itineraryQueryService.handle(any(GenerateItineraryQuery.class))).thenReturn(itinerary);

        // Act
        ResponseEntity<ItineraryResource> response = controller.generateItinerary(resource);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().destinationId());
        assertEquals(1, response.getBody().days().size());
    }

    @Test
    void generateItinerary_returnsBadRequest_whenNoExperiencesFound() {
        // Arrange
        var resource = new GenerateItineraryResource(99L, List.of("AVENTURA"), 2);
        when(itineraryQueryService.handle(any(GenerateItineraryQuery.class)))
                .thenThrow(new IllegalStateException("No experiences were found"));

        // Act
        ResponseEntity<ItineraryResource> response = controller.generateItinerary(resource);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void generateItinerary_returnsBadRequest_whenIllegalArgumentThrown() {
        // Arrange
        var resource = new GenerateItineraryResource(1L, List.of("CULTURA"), 1);
        when(itineraryQueryService.handle(any(GenerateItineraryQuery.class)))
                .thenThrow(new IllegalArgumentException("Number of days must be greater than zero"));

        // Act
        ResponseEntity<ItineraryResource> response = controller.generateItinerary(resource);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}