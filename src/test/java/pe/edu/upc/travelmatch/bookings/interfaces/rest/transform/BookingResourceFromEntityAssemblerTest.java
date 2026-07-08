package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.BookingResource;

@DisplayName("BookingResourceFromEntityAssembler Tests")
class BookingResourceFromEntityAssemblerTest {

  @Test
  @DisplayName("Debe mapear una Entidad Booking a un Resource correctamente")
  void toResourceFromEntity_validEntity_mapsSuccessfully() {
    // --- Arrange (Preparar) ---
    Booking entity = mock(Booking.class);
    Instant now = Instant.now();

    // 1. Mockeamos el ID y campos básicos
    when(entity.getId()).thenReturn(1L);
    when(entity.getQuantity()).thenReturn(2);
    when(entity.getBookingDate()).thenReturn(now);

    // 2. Mockeamos UserId
    UserId userIdMock = mock(UserId.class);
    when(userIdMock.userId()).thenReturn(10L);
    when(entity.getUserId()).thenReturn(userIdMock);

    // 3. Mockeamos AvailabilityId
    AvailabilityId availabilityIdMock = mock(AvailabilityId.class);
    when(availabilityIdMock.availabilityId()).thenReturn(5L);
    when(entity.getAvailabilityId()).thenReturn(availabilityIdMock);

    Money moneyMock = mock(Money.class);
    when(moneyMock.getCurrency()).thenReturn("USD");
    when(moneyMock.getAmount())
        .thenReturn(
            BigDecimal.valueOf(
                250.00)); // Se asume que es Double o BigDecimal, ajusta si es necesario
    when(entity.getTotalBookingPrice()).thenReturn(moneyMock);

    when(entity.getBookingStatus())
        .thenReturn(
            BookingStatus.SUCCEEDED); // Asume que existe CONFIRMED u otro estado válido en tu enum

    // --- Act (Ejecutar) ---
    BookingResource resource = BookingResourceFromEntityAssembler.toResourceFromEntity(entity);

    // --- Assert (Verificar) ---
    assertNotNull(resource, "El Resource no debe ser nulo");
    assertEquals(1L, resource.id(), "El ID debe coincidir");
    assertEquals(10L, resource.userId(), "El UserId debe coincidir");
    assertEquals(5L, resource.availabilityId(), "El AvailabilityId debe coincidir");
    assertEquals(2, resource.quantity(), "La cantidad debe coincidir");
    assertEquals("USD", resource.currency(), "La moneda debe coincidir");
    assertEquals("SUCCEEDED", resource.bookingStatus(), "El estado de la reserva debe coincidir");
    assertEquals(now, resource.bookingDate(), "La fecha debe coincidir");
  }
}
