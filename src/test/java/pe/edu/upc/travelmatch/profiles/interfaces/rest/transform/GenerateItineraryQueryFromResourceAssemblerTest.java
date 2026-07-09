package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.GenerateItineraryResource;

class GenerateItineraryQueryFromResourceAssemblerTest {

  @Test
  @DisplayName("toQueryFromResource should map GenerateItineraryResource to GenerateItineraryQuery")
  void toQueryFromResource_ShouldMapCorrectly() {
    var resource = new GenerateItineraryResource(3L, List.of("GASTRONOMIA"), 4);

    var query = GenerateItineraryQueryFromResourceAssembler.toQueryFromResource(resource);

    assertNotNull(query);
    assertEquals(3L, query.destinationId());
    assertEquals(List.of("GASTRONOMIA"), query.categories());
    assertEquals(4, query.numberOfDays());
  }
}