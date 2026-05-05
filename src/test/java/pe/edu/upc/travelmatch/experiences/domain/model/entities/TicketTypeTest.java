package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TicketTypeTest {

    @Test
    void testTicketTypeConstructorAndGetters() {
        // Arrange
        TicketTypes expectedType = TicketTypes.TICKET_GENERAL;

        // Act
        TicketType ticketType = new TicketType(expectedType);

        // Assert
        assertEquals(expectedType, ticketType.getName());
        assertEquals("TICKET_GENERAL", ticketType.getTicketTypeName());
        assertNull(ticketType.getId()); // Should be null before persisting
    }

    @Test
    void testGetDefaultTicketType() {
        // Act
        TicketType ticketType = TicketType.getDefaultTicketType();

        // Assert
        assertEquals(TicketTypes.TICKET_GENERAL, ticketType.getName());
    }

    @Test
    void testToTicketTypeFromName() {
        // Arrange
        String name = "TICKET_GENERAL";

        // Act
        TicketType ticketType = TicketType.toTicketTypeFromName(name);

        // Assert
        assertEquals(TicketTypes.TICKET_GENERAL, ticketType.getName());
    }

    @Test
    void testValidateTicketTypeSetWhenEmpty() {
        // Act
        List<TicketType> result = TicketType.validateTicketTypeSet(List.of());

        // Assert
        assertEquals(1, result.size());
        assertEquals(TicketTypes.TICKET_GENERAL, result.getFirst().getName());
    }

    @Test
    void testValidateTicketTypeSetWhenNotEmpty() {
        // Arrange
        List<TicketType> providedList = List.of(new TicketType(TicketTypes.TICKET_VIP));

        // Act
        List<TicketType> result = TicketType.validateTicketTypeSet(providedList);

        // Assert
        assertEquals(providedList.size(), result.size());
        assertEquals(TicketTypes.TICKET_VIP, result.getFirst().getName());
    }
}

