package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateAvailabilityResource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateAvailabilityCommandFromResourceAssemblerTest {

  @Test
  @DisplayName(
      "toCommandFromResource should map CreateAvailabilityResource to CreateAvailabilityCommand"
          + " (AAA)")
  void toCommandFromResource_ShouldMap() {
    // Arrange
    var resource =
        new CreateAvailabilityResource(
            1L,
            LocalDateTime.of(2024, 7, 15, 12, 31, 32),
            LocalDateTime.of(2025, 7, 15, 12, 31, 32),
            20);
    Experience exp = mock(Experience.class);
    when(exp.getId()).thenReturn(1L);
    // Act
    CreateAvailabilityCommand cmd =
        CreateAvailabilityCommandFromResourceAssembler.toCommandFromResource(resource, exp);

    // Assert
    assertNotNull(cmd);
    assertEquals(1L, cmd.experience().getId());
    assertEquals(LocalDateTime.of(2024, 7, 15, 12, 31, 32), cmd.startDateTime());
    assertEquals(LocalDateTime.of(2025, 7, 15, 12, 31, 32), cmd.endDateTime());
    assertEquals(20, cmd.capacity());
  }
}
