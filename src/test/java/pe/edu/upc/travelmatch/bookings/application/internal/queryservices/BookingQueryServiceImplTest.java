package pe.edu.upc.travelmatch.bookings.application.internal.queryservices;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingByIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingsByUserIdQuery;
import pe.edu.upc.travelmatch.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingQueryServiceImpl Tests")
public class BookingQueryServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingQueryServiceImpl bookingQueryService;

    @Test
    @DisplayName("Debe retornar una reserva por su ID")
    void handle_getBookingById_returnsOptional() {
        // --- Arrange (Preparar) ---
        GetBookingByIdQuery query = new GetBookingByIdQuery(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mock(Booking.class)));

        // --- Act (Ejecutar) ---
        Optional<Booking> result = bookingQueryService.handle(query);

        // --- Assert (Verificar) ---
        assertTrue(result.isPresent(), "Debe encontrar la reserva mockeada");
        verify(bookingRepository, times(1)).findById(1L);
    }


}
