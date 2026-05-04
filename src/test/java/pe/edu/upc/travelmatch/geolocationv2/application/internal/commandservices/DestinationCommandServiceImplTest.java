package pe.edu.upc.travelmatch.geolocationv2.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.DeleteDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.UpdateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.DestinationName;
import pe.edu.upc.travelmatch.geolocationv2.infrastructure.persistence.jpa.repositories.DestinationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Implementation of the DestinationCommandService interface for handling destination-related commands.
 *
 * <p>This service provides methods to create, update, and delete destination entities, interacting
 * with the DestinationRepository.</p>
 */
@ExtendWith(MockitoExtension.class)
class DestinationCommandServiceImplTest {

    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private DestinationCommandServiceImpl destinationCommandService;

    // ----- CreateDestinationCommand Tests -----

    @Test
    @DisplayName("handle(CreateDestinationCommand) should return destination ID when created successfully")
    void handle_CreateDestinationCommand_ShouldReturnId_WhenCreatedSuccessfully() {
        // Arrange
        var command = new CreateDestinationCommand("Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");
        var name = new DestinationName(command.name());

        when(destinationRepository.existsByName(name)).thenReturn(false);

        // Act
        var result = destinationCommandService.handle(command);

        // Assert
        verify(destinationRepository).existsByName(name);
        verify(destinationRepository).save(any(Destination.class));
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    @DisplayName("handle(CreateDestinationCommand) should throw IllegalArgumentException when name already exists")
    void handle_CreateDestinationCommand_ShouldThrowException_WhenNameAlreadyExists() {
        // Arrange
        var command = new CreateDestinationCommand("Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");
        var name = new DestinationName(command.name());

        when(destinationRepository.existsByName(name)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> destinationCommandService.handle(command), "Destination with name " + name + " already exists");

        verify(destinationRepository).existsByName(name);
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    @DisplayName("handle(CreateDestinationCommand) should throw IllegalArgumentException when save fails")
    void handle_CreateDestinationCommand_ShouldThrowException_WhenSaveFails() {
        // Arrange
        var command = new CreateDestinationCommand("Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");
        var name = new DestinationName(command.name());

        when(destinationRepository.existsByName(name)).thenReturn(false);
        when(destinationRepository.save(any(Destination.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> destinationCommandService.handle(command), "Error while saving profile: Database error");

        verify(destinationRepository).existsByName(name);
        verify(destinationRepository).save(any(Destination.class));
        verifyNoMoreInteractions(destinationRepository);
    }

    // ----- UpdateDestinationCommand Tests -----

    @Test
    @DisplayName("handle(UpdateDestinationCommand) should return updated destination when successful")
    void handle_UpdateDestinationCommand_ShouldReturnUpdatedDestination_WhenSuccessful() {
        // Arrange
        var command = new UpdateDestinationCommand(1L, "Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");
        var name = new DestinationName(command.name());
        var existingDestination = mock(Destination.class);

        when(destinationRepository.existsByNameAndIdIsNot(name, command.destinationId())).thenReturn(false);
        when(destinationRepository.existsById(command.destinationId())).thenReturn(true);
        when(destinationRepository.findById(command.destinationId())).thenReturn(Optional.of(existingDestination));
        when(destinationRepository.save(existingDestination)).thenReturn(existingDestination);

        // Act
        var result = destinationCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(existingDestination, result.get());

        verify(destinationRepository).existsByNameAndIdIsNot(name, command.destinationId());
        verify(destinationRepository).existsById(command.destinationId());
        verify(destinationRepository).findById(command.destinationId());
        verify(existingDestination).updateInformation(command.name(), command.address(), command.district(), command.city(), command.state(), command.country());
        verify(destinationRepository).save(existingDestination);
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    @DisplayName("handle(UpdateDestinationCommand) should throw IllegalArgumentException when name already exists for another ID")
    void handle_UpdateDestinationCommand_ShouldThrowException_WhenNameExistsForAnotherId() {
        // Arrange
        var command = new UpdateDestinationCommand(1L, "Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");
        var name = new DestinationName(command.name());

        when(destinationRepository.existsByNameAndIdIsNot(name, command.destinationId())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> destinationCommandService.handle(command), "Destination with name " + name + " already exists");

        verify(destinationRepository).existsByNameAndIdIsNot(name, command.destinationId());
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    @DisplayName("handle(UpdateDestinationCommand) should throw IllegalArgumentException when ID does not exist")
    void handle_UpdateDestinationCommand_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        var command = new UpdateDestinationCommand(1L, "Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");
        var name = new DestinationName(command.name());

        when(destinationRepository.existsByNameAndIdIsNot(name, command.destinationId())).thenReturn(false);
        when(destinationRepository.existsById(command.destinationId())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> destinationCommandService.handle(command), "Destination with id 1 does not exist");

        verify(destinationRepository).existsByNameAndIdIsNot(name, command.destinationId());
        verify(destinationRepository).existsById(command.destinationId());
        verifyNoMoreInteractions(destinationRepository);
    }

    // ----- DeleteDestinationCommand Tests -----

    @Test
    @DisplayName("handle(DeleteDestinationCommand) should delete destination when successful")
    void handle_DeleteDestinationCommand_ShouldDelete_WhenSuccessful() {
        // Arrange
        var command = new DeleteDestinationCommand(1L);

        when(destinationRepository.existsById(command.destinationId())).thenReturn(true);

        // Act
        destinationCommandService.handle(command);

        // Assert
        verify(destinationRepository).existsById(command.destinationId());
        verify(destinationRepository).deleteById(command.destinationId());
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    @DisplayName("handle(DeleteDestinationCommand) should throw IllegalArgumentException when ID does not exist")
    void handle_DeleteDestinationCommand_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        var command = new DeleteDestinationCommand(1L);

        when(destinationRepository.existsById(command.destinationId())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> destinationCommandService.handle(command), "Destination with id 1 does not exist");

        verify(destinationRepository).existsById(command.destinationId());
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    @DisplayName("handle(DeleteDestinationCommand) should throw IllegalArgumentException when delete fails")
    void handle_DeleteDestinationCommand_ShouldThrowException_WhenDeleteFails() {
        // Arrange
        var command = new DeleteDestinationCommand(1L);

        when(destinationRepository.existsById(command.destinationId())).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(destinationRepository).deleteById(command.destinationId());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> destinationCommandService.handle(command), "Error while deleting destination: Database error");

        verify(destinationRepository).existsById(command.destinationId());
        verify(destinationRepository).deleteById(command.destinationId());
        verifyNoMoreInteractions(destinationRepository);
    }
}
