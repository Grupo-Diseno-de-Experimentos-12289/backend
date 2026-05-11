package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityTicketTypeCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityTicketTypeCommandServiceImplTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private AvailabilityTicketTypeCommandServiceImpl commandService;

    private Availability availability;
    private TicketType ticketType;

    @BeforeEach
    void setUp() {
        availability = new Availability();
        ticketType = new TicketType();
    }

    @Test
    void handle_creaTicketTypeDeDisponibilidadConExito() {
        // Arrange
        CreateAvailabilityTicketTypeCommand command = new CreateAvailabilityTicketTypeCommand(
                1L, 2L, BigDecimal.valueOf(100.0), 50
        );

        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availability));
        when(ticketTypeRepository.findById(2L)).thenReturn(Optional.of(ticketType));

        // Act
        commandService.handle(command);

        // Assert
        verify(availabilityRepository, times(1)).findById(1L);
        verify(ticketTypeRepository, times(1)).findById(2L);
        verify(availabilityRepository, times(1)).save(availability);
    }

    @Test
    void handle_cuandoDisponibilidadNoExiste_lanzaExcepcion() {
        // Arrange
        CreateAvailabilityTicketTypeCommand command = new CreateAvailabilityTicketTypeCommand(
                1L, 2L, BigDecimal.valueOf(100.0), 50
        );

        when(availabilityRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> commandService.handle(command));

        assertTrue(exception.getMessage().contains("does not exist"));
        verify(availabilityRepository, never()).save(any());
    }

    @Test
    void handle_cuandoTicketTypeNoExiste_lanzaExcepcion() {
        // Arrange
        CreateAvailabilityTicketTypeCommand command = new CreateAvailabilityTicketTypeCommand(
                1L, 2L, BigDecimal.valueOf(100.0), 50
        );

        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availability));
        when(ticketTypeRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> commandService.handle(command));

        assertTrue(exception.getMessage().contains("TicketType not found with ID"));
        verify(availabilityRepository, never()).save(any());
    }
}
