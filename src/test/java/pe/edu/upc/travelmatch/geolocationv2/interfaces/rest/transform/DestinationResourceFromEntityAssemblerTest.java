package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.City;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.Country;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.DestinationAddress;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.DestinationName;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.District;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.State;

/**
 * Unit tests for {@link DestinationResourceFromEntityAssembler}.
 *
 * <p>This class validates the proper mapping from a Destination entity to a DestinationResource.
 */
class DestinationResourceFromEntityAssemblerTest {

  @Test
  @DisplayName("toResourceFromEntity should map Destination to DestinationResource correctly")
  void toResourceFromEntity_ShouldMapCorrectly() {
    // Arrange
    var destination = mock(Destination.class);
    when(destination.getId()).thenReturn(1L);
    when(destination.getName()).thenReturn(new DestinationName("Paris"));
    when(destination.getAddress()).thenReturn(new DestinationAddress("123 Street"));
    when(destination.getDistrict()).thenReturn(new District("District 1"));
    when(destination.getCity()).thenReturn(new City("Paris"));
    when(destination.getState()).thenReturn(new State("Ile-de-France"));
    when(destination.getCountry()).thenReturn(new Country("France"));

    // Act
    var resource = DestinationResourceFromEntityAssembler.toResourceFromEntity(destination);

    // Assert
    assertNotNull(resource);
    assertEquals(1L, resource.id());
    assertEquals("Paris", resource.name());
    assertEquals("123 Street", resource.address());
    assertEquals("District 1", resource.district());
    assertEquals("Paris", resource.city());
    assertEquals("Ile-de-France", resource.state());
    assertEquals("France", resource.country());
  }
}
