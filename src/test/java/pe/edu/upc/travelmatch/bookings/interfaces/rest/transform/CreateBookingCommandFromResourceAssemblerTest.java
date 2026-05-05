package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CreateBookingCommand;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.CreateBookingResource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("CreateBookingCommandFromResourceAssembler Tests")
public class CreateBookingCommandFromResourceAssemblerTest {

    @Test
    @DisplayName("Debe mapear un Resource a Command correctamente")
    void toCommandFromResource_validResource_mapsSuccessfully() {

        // --- Arrange (Preparar) ---
        Instant now = Instant.now();
        // 1. Instanciamos el Resource con los 5 argumentos exactos que pide tu Record
        // (userId: 1L, availabilityId: 2L, ticketTypeId: 3L, quantity: 4, bookingDate: now)
        CreateBookingResource resource = new CreateBookingResource(1L, 2L, 3L, 4, now);

        // --- Act (Ejecutar) ---
        // 2. Llamamos directamente al método estático del Assembler
        CreateBookingCommand command = CreateBookingCommandFromResourceAssembler.toCommandFromResource(resource);

        // --- Assert (Verificar) ---
        // 3. Verificamos que no sea nulo y que los 5 campos coincidan a la perfección
        assertNotNull(command, "El comando no debe ser nulo");
        assertEquals(resource.userId(), command.userId(), "El UserID debe coincidir");
        assertEquals(resource.availabilityId(), command.availabilityId(), "El AvailabilityID debe coincidir");
        assertEquals(resource.ticketTypeId(), command.ticketTypeId(), "El TicketTypeId debe coincidir");
        assertEquals(resource.quantity(), command.quantity(), "La cantidad debe coincidir");
        assertEquals(resource.bookingDate(), command.bookingDate(), "La fecha de reserva debe coincidir");
    }
}