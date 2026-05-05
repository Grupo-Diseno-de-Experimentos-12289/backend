package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityTicketTypeTest {

    @Test
    void testAvailabilityTicketTypeConstructorAndGetters() {
        // Arrange
        Experience experience = new Experience();
        Availability availability = new Availability(experience, LocalDateTime.now(), LocalDateTime.now().plusHours(2), 50);
        TicketType ticketType = new TicketType();
        BigDecimal price = new BigDecimal("99.99");
        int stock = 20;

        // Act
        AvailabilityTicketType availabilityTicketType = new AvailabilityTicketType(availability, ticketType, price, stock);

        // Assert
        assertEquals(availability, availabilityTicketType.getAvailability());
        assertEquals(ticketType, availabilityTicketType.getTicketType());
        assertEquals(price, availabilityTicketType.getPrice());
        assertEquals(stock, availabilityTicketType.getStock());
    }

    @Test
    void testSetters() {
        // Arrange
        Availability availability = new Availability();
        TicketType ticketTypeEntity = new TicketType(TicketTypes.TICKET_VIP);
        AvailabilityTicketType ticketType = new AvailabilityTicketType(availability, ticketTypeEntity, new BigDecimal("10.0"), 5);
        BigDecimal newPrice = new BigDecimal("15.00");
        int newStock = 10;

        // Act
        ticketType.setPrice(newPrice);
        ticketType.setStock(newStock);

        // Assert
        assertEquals(newPrice, ticketType.getPrice());
        assertEquals(newStock, ticketType.getStock());
    }

    @Test
    void testReduceStockSuccessfully() {
        // Arrange
        Availability availability = new Availability();
        TicketType ticketTypeEntity = new TicketType();
        AvailabilityTicketType ticketType = new AvailabilityTicketType(availability, ticketTypeEntity, new BigDecimal("10.0"), 50);
        int reduction = 10;

        // Act
        ticketType.reduceStock(reduction);

        // Assert
        assertEquals(40, ticketType.getStock());
    }

    @Test
    void testReduceStockThrowsExceptionWhenNotEnough() {
        // Arrange
        Availability availability = new Availability();
        TicketType ticketTypeEntity = new TicketType();
        AvailabilityTicketType ticketType = new AvailabilityTicketType(availability, ticketTypeEntity, new BigDecimal("10.0"), 5);
        int reduction = 10;

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> ticketType.reduceStock(reduction));
        assertEquals("Not enough stock available.", exception.getMessage());
    }
}
