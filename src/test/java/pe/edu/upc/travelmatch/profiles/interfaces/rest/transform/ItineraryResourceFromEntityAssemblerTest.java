package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Itinerary;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ItineraryActivity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ItineraryDay;

class ItineraryResourceFromEntityAssemblerTest {

  @Test
  @DisplayName("toResourceFromEntity should map Itinerary to ItineraryResource correctly")
  void toResourceFromEntity_ShouldMapCorrectly() {
    var activity = new ItineraryActivity(1L, "Tour Centro", "CULTURA", "Plaza Mayor", "3h");
    var day = new ItineraryDay(1, List.of(activity));
    var itinerary = new Itinerary(5L, 1, List.of(day));

    var resource = ItineraryResourceFromEntityAssembler.toResourceFromEntity(itinerary);

    assertNotNull(resource);
    assertEquals(5L, resource.destinationId());
    assertEquals(1, resource.numberOfDays());
    assertEquals(1, resource.days().size());
    assertEquals(1, resource.days().get(0).dayNumber());
    assertEquals(1L, resource.days().get(0).activities().get(0).experienceId());
    assertEquals("Tour Centro", resource.days().get(0).activities().get(0).title());
  }
}