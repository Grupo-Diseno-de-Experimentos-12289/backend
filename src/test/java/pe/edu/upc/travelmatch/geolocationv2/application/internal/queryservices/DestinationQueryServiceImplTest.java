package pe.edu.upc.travelmatch.geolocationv2.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetAllDestinationsQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByDestinationNameQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByIdQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.DestinationName;
import pe.edu.upc.travelmatch.geolocationv2.infrastructure.persistence.jpa.repositories.DestinationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DestinationQueryServiceImpl}.
 *
 * This class validates the behavior of the DestinationQueryServiceImpl, including
 * fetching destinations by ID, name, or all destinations.
 */
@ExtendWith(MockitoExtension.class)
class DestinationQueryServiceImplTest {

    @Mock private DestinationRepository destinationRepository;
    @InjectMocks private DestinationQueryServiceImpl destinationQueryService;

    @Test
    @DisplayName("handle(GetAllDestinationsQuery) should return list of destinations")
    void handle_GetAllDestinationsQuery_ShouldReturnListOfDestinations() {
        // Arrange
        var query = new GetAllDestinationsQuery();
        var destinationMock = mock(Destination.class);
        var expectedDestinations = List.of(destinationMock);

        when(destinationRepository.findAll()).thenReturn(expectedDestinations);

        // Act
        var result = destinationQueryService.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDestinations, result);

        verify(destinationRepository).findAll();
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    @DisplayName("handle(GetDestinationByDestinationNameQuery) should return destination when found")
    void handle_GetDestinationByDestinationNameQuery_ShouldReturnDestination_WhenFound() {
        // Arrange
        var query = new GetDestinationByDestinationNameQuery("Paris");
        var destinationName = new DestinationName(query.name());
        var destinationMock = mock(Destination.class);

        when(destinationRepository.findByName(destinationName)).thenReturn(Optional.of(destinationMock));

        // Act
        var result = destinationQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(destinationMock, result.get());

        verify(destinationRepository).findByName(destinationName);
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    @DisplayName("handle(GetDestinationByIdQuery) should return destination when found")
    void handle_GetDestinationByIdQuery_ShouldReturnDestination_WhenFound() {
        // Arrange
        var query = new GetDestinationByIdQuery(1L);
        var destinationMock = mock(Destination.class);

        when(destinationRepository.findById(query.destinationId())).thenReturn(Optional.of(destinationMock));

        // Act
        var result = destinationQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(destinationMock, result.get());

        verify(destinationRepository).findById(query.destinationId());
        verifyNoMoreInteractions(destinationRepository);
    }
}
