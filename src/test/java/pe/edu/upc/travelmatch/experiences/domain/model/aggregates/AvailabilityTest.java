package pe.edu.upc.travelmatch.experiences.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityTest {
    @Mock
    private Experience experience;

    @Mock
    private TicketType ticketType;

    @Mock
    private TicketType anotherTicketType;

    @Test
    void testAvailabilityConstructorAndGetters() {
        // Arrange
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        int capacity = 50;

        // Act
        Availability availability = new Availability(experience, start, end, capacity);

        // Assert
        assertEquals(experience, availability.getExperience());
        assertEquals(start, availability.getStartDateTime());
        assertEquals(end, availability.getEndDateTime());
        assertEquals(capacity, availability.getCapacity());
        assertTrue(availability.getTicketTypes().isEmpty());
    }

    @Test
    void testAddTicketType_Successfully() {
        // Arrange
        Availability availability = new Availability(experience, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 20);
        BigDecimal price = new BigDecimal("100.00");
        int stock = 10;

        // Act
        availability.addTicketType(ticketType, price, stock);

        // Assert
        assertEquals(1, availability.getTicketTypes().size());
        var addedTicket = availability.getTicketTypes().iterator().next();
        assertEquals(ticketType, addedTicket.getTicketType());
        assertEquals(price, addedTicket.getPrice());
        assertEquals(stock, addedTicket.getStock());
    }

    @Test
    void testUpdateTicketTypePriceAndStock_Successfully() {
        // Arrange
        Availability availability = new Availability(experience, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 20);
        availability.addTicketType(ticketType, new BigDecimal("50.0"), 5);
        BigDecimal newPrice = new BigDecimal("75.0");
        int newStock = 15;

        // Act
        availability.updateTicketTypePriceAndStock(ticketType, newPrice, newStock);

        // Assert
        var updated = availability.getTicketTypes().iterator().next();
        assertEquals(newPrice, updated.getPrice());
        assertEquals(newStock, updated.getStock());
    }

    @Test
    void testUpdateTicketType_ThrowsExceptionWhenNotFound() {
        // Arrange
        Availability availability = new Availability(experience, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 20);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> availability.updateTicketTypePriceAndStock(ticketType, BigDecimal.TEN, 10));
        assertTrue(exception.getMessage().contains("TicketType not found"));
    }

    @Test
    void testDecrementStock_Successfully() {
        // Arrange
        Availability availability = new Availability(experience, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 20);
        availability.addTicketType(ticketType, new BigDecimal("10.0"), 10);

        // Act
        availability.decrementStock(ticketType, 3);

        // Assert
        var ticket = availability.getTicketTypes().iterator().next();
        assertEquals(7, ticket.getStock());
    }

    @Test
    void testUpdateInfo_Successfully() {
        // Arrange
        Availability availability = new Availability(experience, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 20);
        LocalDateTime newStart = LocalDateTime.now().plusDays(1);
        LocalDateTime newEnd = newStart.plusHours(2);
        int newCapacity = 100;

        // Act
        availability.updateInfo(newStart, newEnd, newCapacity);

        // Assert
        assertEquals(newStart, availability.getStartDateTime());
        assertEquals(newEnd, availability.getEndDateTime());
        assertEquals(newCapacity, availability.getCapacity());
    }

    @Test
    void testMarkAsDeleted_Successfully() {
        // Arrange
        Availability availability = new Availability(experience, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 20);

        // Act
        availability.markAsDeleted();

        // Assert
        assertNotNull(availability.getDeletedAt());
    }
    
}
