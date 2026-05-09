package pe.edu.upc.travelmatch.experiences.domain.model.entities;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AvailabilityTicketTypeTest {

    @Mock
    private Availability availability;

    @Mock
    private TicketType ticketType;

    @Test
    void testAvailabilityTicketTypeConstructorAndGetters() {
        // Arrange
        BigDecimal price = new BigDecimal("150.00");
        int stock = 50;

        // Act
        AvailabilityTicketType availabilityTicketType = new AvailabilityTicketType(availability, ticketType, price, stock);

        // Assert
        assertEquals(availability, availabilityTicketType.getAvailability());
        assertEquals(ticketType, availabilityTicketType.getTicketType());
        assertEquals(price, availabilityTicketType.getPrice());
        assertEquals(stock, availabilityTicketType.getStock());
    }

    @Test
    void testReduceStock_Successfully() {
        // Arrange
        int initialStock = 10;
        int quantityToReduce = 4;
        AvailabilityTicketType availabilityTicketType = new AvailabilityTicketType(availability, ticketType, BigDecimal.TEN, initialStock);

        // Act
        availabilityTicketType.reduceStock(quantityToReduce);

        // Assert
        assertEquals(6, availabilityTicketType.getStock());
    }

    @Test
    void testReduceStock_ThrowsExceptionWhenNotEnoughStock() {
        // Arrange
        int initialStock = 5;
        int quantityToReduce = 10;
        AvailabilityTicketType availabilityTicketType = new AvailabilityTicketType(availability, ticketType, BigDecimal.TEN, initialStock);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> availabilityTicketType.reduceStock(quantityToReduce));

        assertTrue(exception.getMessage().contains("Not enough stock available"));
        assertEquals(initialStock, availabilityTicketType.getStock()); // El stock no debería cambiar
    }
    
}
