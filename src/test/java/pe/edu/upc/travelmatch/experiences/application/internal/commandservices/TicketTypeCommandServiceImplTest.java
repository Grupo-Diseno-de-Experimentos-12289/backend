package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedTicketTypesCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TicketTypeCommandServiceImplTest {

    private TicketTypeRepository ticketTypeRepository;
    private TicketTypeCommandServiceImpl ticketTypeCommandService;

    @BeforeEach
    void setUp() {
        ticketTypeRepository = mock(TicketTypeRepository.class);
        ticketTypeCommandService = new TicketTypeCommandServiceImpl(ticketTypeRepository);
    }

    @Test
    void testHandleSeedTicketTypesCommand_CreatesMissingTicketTypes() {
        // Arrange
        when(ticketTypeRepository.existsByName(any())).thenReturn(false);

        // Act
        ticketTypeCommandService.handle(new SeedTicketTypesCommand());

        // Assert
        ArgumentCaptor<TicketType> captor = ArgumentCaptor.forClass(TicketType.class);
        verify(ticketTypeRepository, times(TicketTypes.values().length)).save(captor.capture());

        var savedTypes = captor.getAllValues();
        assertEquals(TicketTypes.values().length, savedTypes.size());
    }

    @Test
    void testHandleSeedTicketTypesCommand_SkipsExistingTicketTypes() {
        // Arrange
        when(ticketTypeRepository.existsByName(any())).thenReturn(true);

        // Act
        ticketTypeCommandService.handle(new SeedTicketTypesCommand());

        // Assert
        verify(ticketTypeRepository, never()).save(any(TicketType.class));
    }
}