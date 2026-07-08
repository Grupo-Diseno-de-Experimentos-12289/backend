package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAvailabilitiesByExperienceIdQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAvailabilityByIdQuery;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityQueryServiceImplTest {

  @Mock private AvailabilityRepository availabilityRepository;

  @InjectMocks private AvailabilityQueryServiceImpl availabilityQueryService;

  @Test
  void getAllAvailabilities_retornaListadoDeDisponibilidades() {
    // Arrange
    Availability a1 = new Availability();
    Availability a2 = new Availability();
    List<Availability> expectedList = Arrays.asList(a1, a2);

    when(availabilityRepository.findAllByDeletedAtIsNull()).thenReturn(expectedList);

    // Act
    List<Availability> result = availabilityQueryService.getAllAvailabilities();

    // Assert
    assertEquals(2, result.size());
    verify(availabilityRepository, times(1)).findAllByDeletedAtIsNull();
  }

  @Test
  void handle_retornaDisponibilidadPorId() {
    // Arrange
    Long id = 1L;
    GetAvailabilityByIdQuery query = new GetAvailabilityByIdQuery(id);
    Availability availability = new Availability();

    when(availabilityRepository.findById(id)).thenReturn(Optional.of(availability));

    // Act
    Optional<Availability> result = availabilityQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    verify(availabilityRepository, times(1)).findById(id);
  }

  @Test
  void handle_retornaDisponibilidadesPorExperienceId() {
    // Arrange
    Long experienceId = 5L;
    Availability a1 = new Availability();
    Availability a2 = new Availability();
    List<Availability> expectedList = Arrays.asList(a1, a2);

    when(availabilityRepository.findAllByExperience_IdAndDeletedAtIsNullOrderByStartDateTimeAsc(
            experienceId))
        .thenReturn(expectedList);

    // Act
    List<Availability> result =
        availabilityQueryService.handle(new GetAvailabilitiesByExperienceIdQuery(experienceId));

    // Assert
    assertEquals(2, result.size());
    verify(availabilityRepository, times(1))
        .findAllByExperience_IdAndDeletedAtIsNullOrderByStartDateTimeAsc(experienceId);
  }
}
