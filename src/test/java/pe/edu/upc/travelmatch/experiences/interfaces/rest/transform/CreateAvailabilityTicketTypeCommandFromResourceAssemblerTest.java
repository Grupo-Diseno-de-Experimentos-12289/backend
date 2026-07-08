package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityTicketTypeCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateAvailabilityTicketTypeResource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateAvailabilityTicketTypeCommandFromResourceAssemblerTest {

  @Test
  @DisplayName(
      "toCommandFromResource should map CreateAvailabilityTicketTypeResource to"
          + " CreateAvailabilityTicketTypeCommand (AAA)")
  void toCommandFromResource_ShouldMap() {
    // Arrange
    Availability avb = mock(Availability.class);
    BigDecimal price = new BigDecimal("150.00");
    var resource = new CreateAvailabilityTicketTypeResource(1L, 3L, "Type1", price, 50);
    when(avb.getId()).thenReturn(1L);

    // Act

    CreateAvailabilityTicketTypeCommand tick =
        CreateAvailabilityTicketTypeCommandFromResourceAssembler.toCommandFromResource(
            resource, avb);

    // Assert
    assertNotNull(tick);
    assertEquals(1L, tick.availabilityId());
    assertEquals(3L, tick.ticketTypeId());
    assertEquals(50, tick.stock());
    assertEquals(price, tick.price());
  }
}
