package pe.edu.upc.travelmatch.bookings.application.internal.queryservices;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingByIdQuery;
import pe.edu.upc.travelmatch.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingQueryServiceImpl Tests")
class BookingQueryServiceImplTest {
  @Mock private BookingRepository bookingRepository;

  @InjectMocks private BookingQueryServiceImpl bookingQueryService;

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
