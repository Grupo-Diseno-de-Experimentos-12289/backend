package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;

@ExtendWith(MockitoExtension.class)
class TicketTypeTest {

  @Test
  void testTicketTypeConstructorAndGetters() {
    // Arrange
    TicketTypes expectedType = TicketTypes.TICKET_VIP;

    // Act
    TicketType ticketType = new TicketType(expectedType);

    // Assert
    assertEquals(expectedType, ticketType.getName());
  }

  @Test
  void testGetTicketTypeName_ReturnsStringRepresentation() {
    // Arrange
    TicketType ticketType = new TicketType(TicketTypes.TICKET_GENERAL);

    // Act
    String name = ticketType.getTicketTypeName();

    // Assert
    assertEquals("TICKET_GENERAL", name);
  }

  @Test
  void testGetDefaultTicketType_ReturnsTicketGeneral() {
    // Act
    TicketType defaultType = TicketType.getDefaultTicketType();

    // Assert
    assertNotNull(defaultType);
    assertEquals(TicketTypes.TICKET_GENERAL, defaultType.getName());
  }

  @Test
  void testToTicketTypeFromName_ReturnsCorrectEnum() {
    // Arrange
    String typeName = "TICKET_VIP";

    // Act
    TicketType result = TicketType.toTicketTypeFromName(typeName);

    // Assert
    assertNotNull(result);
    assertEquals(TicketTypes.TICKET_VIP, result.getName());
  }

  @Test
  void testValidateTicketTypeSet_ReturnsDefaultWhenListIsEmpty() {
    // Arrange
    List<TicketType> emptyList = Collections.emptyList();

    // Act
    List<TicketType> result = TicketType.validateTicketTypeSet(emptyList);

    // Assert
    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals(TicketTypes.TICKET_GENERAL, result.get(0).getName()));
  }

  @Test
  void testValidateTicketTypeSet_ReturnsSameListWhenNotEmpty() {
    // Arrange
    TicketType vipType = new TicketType(TicketTypes.TICKET_VIP);
    List<TicketType> ticketTypeList = List.of(vipType);

    // Act
    List<TicketType> result = TicketType.validateTicketTypeSet(ticketTypeList);

    // Assert
    assertAll(() -> assertEquals(1, result.size()), () -> assertEquals(vipType, result.get(0)));
  }
}
