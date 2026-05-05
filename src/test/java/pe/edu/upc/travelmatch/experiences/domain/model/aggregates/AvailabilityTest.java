package pe.edu.upc.travelmatch.experiences.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityTest {

    @Test
    void testAvailabilityConstructorAndGetters() {
        // Arrange
        Experience experience = new Experience();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        int capacity = 100;

        // Act
        Availability availability = new Availability(experience, start, end, capacity);

        // Assert
        assertEquals(experience, availability.getExperience());
        assertEquals(start, availability.getStartDateTime());
        assertEquals(end, availability.getEndDateTime());
        assertEquals(capacity, availability.getCapacity());
        assertNull(availability.getDeletedAt());
        assertNotNull(availability.getTicketTypes());
        assertTrue(availability.getTicketTypes().isEmpty());
    }

    @Test
    void testAddTicketType() {
        // Arrange
        Experience experience = new Experience();
        Availability availability = new Availability(experience, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 50);
        TicketType ticketType = new TicketType();
        BigDecimal price = new BigDecimal("49.99");
        int stock = 10;

        // Act
        availability.addTicketType(ticketType, price, stock);

        // Assert
        assertEquals(1, availability.getTicketTypes().size());
        assertTrue(availability.getTicketTypes().stream().anyMatch(t -> t.getTicketType().equals(ticketType)));
    }

    @Test
    void testUpdateInfo() {
        // Arrange
        Experience experience = new Experience();
        Availability availability = new Availability(experience, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 50);
        LocalDateTime newStart = LocalDateTime.now().plusDays(1);
        LocalDateTime newEnd = newStart.plusHours(3);
        int newCapacity = 75;

        // Act
        availability.updateInfo(newStart, newEnd, newCapacity);

        // Assert
        assertEquals(newStart, availability.getStartDateTime());
        assertEquals(newEnd, availability.getEndDateTime());
        assertEquals(newCapacity, availability.getCapacity());
    }

    @Test
    void testMarkAsDeleted() {
        // Arrange
        Availability availability = new Availability();

        // Act
        availability.markAsDeleted();

        // Assert
        assertNotNull(availability.getDeletedAt());
    }

    @Test
    void testUpdateTicketTypePriceAndStock() {
        // Arrange
        Availability availability = new Availability();
        TicketType ticketType = new TicketType(TicketTypes.TICKET_GENERAL);
        availability.addTicketType(ticketType, new BigDecimal("50.00"), 10);

        BigDecimal newPrice = new BigDecimal("60.00");
        int newStock = 20;

        // Act
        availability.updateTicketTypePriceAndStock(ticketType, newPrice, newStock);

        // Assert
        availability.getTicketTypes().forEach(at -> {
            if (at.getTicketType().equals(ticketType)) {
                assertEquals(newPrice, at.getPrice());
                assertEquals(newStock, at.getStock());
            }
        });
    }

    @Test
    void testDecrementStock() {
        // Arrange
        Availability availability = new Availability();
        TicketType ticketType = new TicketType(TicketTypes.TICKET_GENERAL);
        availability.addTicketType(ticketType, new BigDecimal("50.00"), 20);

        // Act
        availability.decrementStock(ticketType, 5);

        // Assert
        availability.getTicketTypes().forEach(at -> {
            if (at.getTicketType().equals(ticketType)) {
                assertEquals(15, at.getStock());
            }
        });
    }
}
