package pe.edu.upc.travelmatch.bookings.domain.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.bookings.application.internal.queryservices.BookingQueryServiceImpl;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Payment;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Payout;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Refund;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingByIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingsByUserIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.*;
import pe.edu.upc.travelmatch.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("BookingQueryServiceImpl")
public class BookingQueryServiceTests {
    private static final Money MONEY = new Money(new BigDecimal("100.00"), "PEN");
    @Mock private BookingRepository bookingRepository;
    @InjectMocks private BookingQueryServiceImpl bookingQueryService;

    private Booking booking;

    @BeforeEach
    void arrange_sharedBooking() {
        booking = new Booking(
                new UserId(1L), new AvailabilityId(10L),
                MONEY, 2, BookingStatus.PENDING, Instant.now()
        );
    }

    @Test
    @DisplayName("handle(GetBookingByIdQuery) returns booking when it exists")
    void handle_existingId_returnsBooking() {
        // Arrange
        GetBookingByIdQuery query = new GetBookingByIdQuery(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Act
        Optional<Booking> result = bookingQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(booking, result.get());
    }

    @Test
    @DisplayName("handle(GetBookingByIdQuery) returns empty when booking not found")
    void handle_nonExistingId_returnsEmpty() {
        // Arrange
        GetBookingByIdQuery query = new GetBookingByIdQuery(999L);
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Booking> result = bookingQueryService.handle(query);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("handle(GetBookingByIdQuery) delegates to repository with correct id")
    void handle_query_callsRepositoryWithCorrectId() {
        // Arrange
        GetBookingByIdQuery query = new GetBookingByIdQuery(42L);
        when(bookingRepository.findById(42L)).thenReturn(Optional.empty());

        // Act
        bookingQueryService.handle(query);

        // Assert
        verify(bookingRepository, times(1)).findById(42L);
    }

    @Test
    @DisplayName("handle(GetBookingsByUserIdQuery) returns list when user has bookings")
    void handle_userWithBookings_returnsList() {
        // Arrange
        UserId userId = new UserId(1L);
        GetBookingsByUserIdQuery query = new GetBookingsByUserIdQuery(userId);
        when(bookingRepository.findAllByUserId(userId)).thenReturn(List.of(booking));

        // Act
        List<Booking> result = bookingQueryService.handle(query);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("handle(GetBookingsByUserIdQuery) returns empty list when no bookings exist")
    void handle_userWithNoBookings_returnsEmptyList() {
        // Arrange
        UserId userId = new UserId(2L);
        GetBookingsByUserIdQuery query = new GetBookingsByUserIdQuery(userId);
        when(bookingRepository.findAllByUserId(userId)).thenReturn(List.of());

        // Act
        List<Booking> result = bookingQueryService.handle(query);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("GetBookingsByUserIdQuery throws when userId is null")
    void query_nullUserId_throwsIllegalArgumentException() {
        // Arrange – nothing

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new GetBookingsByUserIdQuery(null));


        assertTrue(exception.getMessage().contains("must not be null"));
    }

    @Test
    @DisplayName("GetBookingsByUserIdQuery throws when userId value is 0")
    void query_zeroUserId_throwsIllegalArgument() {
        // Arrange – nothing

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new GetBookingsByUserIdQuery(new UserId(0L)));
    }
}
