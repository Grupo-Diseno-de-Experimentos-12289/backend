package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.DeleteDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.UpdateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetAllDestinationsQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByIdQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.services.DestinationCommandService;
import pe.edu.upc.travelmatch.geolocationv2.domain.services.DestinationQueryService;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.CreateDestinationResource;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.DestinationResource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DestinationsController}.
 *
 * This class validates the destinations REST endpoints, ensuring correct
 * CRUD operations and HTTP response mappings.
 */
@ExtendWith(MockitoExtension.class)
class DestinationsControllerTest {

    @Mock private DestinationQueryService destinationQueryService;
    @Mock private DestinationCommandService destinationCommandService;
    @InjectMocks private DestinationsController destinationsController;

    @Test
    @DisplayName("createDestination should return 201 Created and DestinationResource when successful")
    void createDestination_ShouldReturnCreatedAndResource_WhenSuccessful() {
        // Arrange
        var createResource = new CreateDestinationResource("Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");
        var expectedId = 1L;
        var destinationSpy = spy(new Destination("Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France"));
        when(destinationSpy.getId()).thenReturn(expectedId);

        when(destinationCommandService.handle(any(CreateDestinationCommand.class))).thenReturn(expectedId);
        when(destinationQueryService.handle(any(GetDestinationByIdQuery.class))).thenReturn(Optional.of(destinationSpy));

        // Act
        ResponseEntity<DestinationResource> response = destinationsController.createDestination(createResource);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Paris", response.getBody().name());
        
        verify(destinationCommandService).handle(any(CreateDestinationCommand.class));
        verify(destinationQueryService).handle(any(GetDestinationByIdQuery.class));
        verifyNoMoreInteractions(destinationCommandService, destinationQueryService);
    }

    @Test
    @DisplayName("createDestination should return 400 Bad Request when creation fails")
    void createDestination_ShouldReturnBadRequest_WhenCreationFailed() {
        // Arrange
        var createResource = new CreateDestinationResource("Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");
        
        when(destinationCommandService.handle(any(CreateDestinationCommand.class))).thenReturn(0L);

        // Act
        ResponseEntity<DestinationResource> response = destinationsController.createDestination(createResource);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(destinationCommandService).handle(any(CreateDestinationCommand.class));
        verifyNoMoreInteractions(destinationCommandService, destinationQueryService);
    }

    @Test
    @DisplayName("getAllDestinations should return 200 OK and list of DestinationResources")
    void getAllDestinations_ShouldReturnOkAndList() {
        // Arrange
        var destinationSpy = spy(new Destination("Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France"));
        when(destinationSpy.getId()).thenReturn(1L);

        when(destinationQueryService.handle(any(GetAllDestinationsQuery.class))).thenReturn(List.of(destinationSpy));

        // Act
        ResponseEntity<List<DestinationResource>> response = destinationsController.getAllDestinations();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Paris", response.getBody().get(0).name());
        
        verify(destinationQueryService).handle(any(GetAllDestinationsQuery.class));
        verifyNoMoreInteractions(destinationCommandService, destinationQueryService);
    }

    @Test
    @DisplayName("getDestinationById should return 200 OK and DestinationResource when found")
    void getDestinationById_ShouldReturnOkAndResource_WhenFound() {
        // Arrange
        var expectedId = 1L;
        var destinationSpy = spy(new Destination("Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France"));
        when(destinationSpy.getId()).thenReturn(expectedId);

        when(destinationQueryService.handle(any(GetDestinationByIdQuery.class))).thenReturn(Optional.of(destinationSpy));

        // Act
        ResponseEntity<DestinationResource> response = destinationsController.getDestinationById(expectedId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Paris", response.getBody().name());
        
        verify(destinationQueryService).handle(any(GetDestinationByIdQuery.class));
        verifyNoMoreInteractions(destinationCommandService, destinationQueryService);
    }

    @Test
    @DisplayName("getDestinationById should return 400 Bad Request when not found")
    void getDestinationById_ShouldReturnBadRequest_WhenNotFound() {
        // Arrange
        var expectedId = 1L;

        when(destinationQueryService.handle(any(GetDestinationByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<DestinationResource> response = destinationsController.getDestinationById(expectedId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(destinationQueryService).handle(any(GetDestinationByIdQuery.class));
        verifyNoMoreInteractions(destinationCommandService, destinationQueryService);
    }

    @Test
    @DisplayName("updateDestination should return 200 OK and updated DestinationResource")
    void updateDestination_ShouldReturnOkAndUpdatedResource() {
        // Arrange
        var expectedId = 1L;
        var resource = new DestinationResource(expectedId, "London", "456 Street", "District 2", "London", "England", "UK");
        var destinationSpy = spy(new Destination("London", "456 Street", "District 2", "London", "England", "UK"));
        when(destinationSpy.getId()).thenReturn(expectedId);

        when(destinationCommandService.handle(any(UpdateDestinationCommand.class))).thenReturn(Optional.of(destinationSpy));

        // Act
        ResponseEntity<DestinationResource> response = destinationsController.updateDestination(expectedId, resource);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("London", response.getBody().name());
        
        verify(destinationCommandService).handle(any(UpdateDestinationCommand.class));
        verifyNoMoreInteractions(destinationCommandService, destinationQueryService);
    }

    @Test
    @DisplayName("deleteDestination should return 204 No Content")
    void deleteDestination_ShouldReturnNoContent() {
        // Arrange
        var expectedId = 1L;
        doNothing().when(destinationCommandService).handle(any(DeleteDestinationCommand.class));

        // Act
        ResponseEntity<?> response = destinationsController.deleteDestination(expectedId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        verify(destinationCommandService).handle(any(DeleteDestinationCommand.class));
        verifyNoMoreInteractions(destinationCommandService, destinationQueryService);
    }
}
