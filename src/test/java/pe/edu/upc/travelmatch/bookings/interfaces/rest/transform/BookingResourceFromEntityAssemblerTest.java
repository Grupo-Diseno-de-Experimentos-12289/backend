package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.BookingResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("BookingResourceFromEntityAssembler Tests")
public class BookingResourceFromEntityAssemblerTest {

    @Test
    @DisplayName("Debe mapear una Entidad Booking a un Resource correctamente")
    void toResourceFromEntity_validEntity_mapsSuccessfully() {
        // --- Arrange (Preparar) ---
        Booking entity = mock(Booking.class);
        when(entity.getId()).thenReturn(1L);
        // when(entity.getQuantity()).thenReturn(2);
        // Simula los getters necesarios de tu entidad según lo que exponga el Resource

        // --- Act (Ejecutar) ---
        BookingResource resource = BookingResourceFromEntityAssembler.toResourceFromEntity(entity);

        // --- Assert (Verificar) ---
        assertNotNull(resource);
        assertEquals(1L, resource.id(), "El ID mapeado debe ser correcto");
    }
}