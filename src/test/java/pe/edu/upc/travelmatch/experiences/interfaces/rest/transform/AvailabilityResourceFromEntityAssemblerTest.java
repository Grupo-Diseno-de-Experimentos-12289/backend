package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.AvailabilityResource;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AvailabilityResourceFromEntityAssemblerTest {

    @Test
    @DisplayName("toResource should map Availability entity to AvailabilityResource (AAA)")
    void toResource_ShouldMapEntityToResource() {

        //Arrange
        Availability entity = mock(Availability.class);
        Experience experience = mock(Experience.class);
        when(entity.getId()).thenReturn(1L);
        when(entity.getExperience()).thenReturn(experience);
        when(experience.getId()).thenReturn(10L);
        when(entity.getStartDateTime()).thenReturn(LocalDateTime.of(2024, 7, 1, 9, 0));
        when(entity.getEndDateTime()).thenReturn(LocalDateTime.of(2024, 7, 1, 17, 0));
        when(entity.getCapacity()).thenReturn(20);
        //Act
        AvailabilityResource resource = AvailabilityResourceFromEntityAssembler.toResourceFromEntity(entity);

        //Assert
        assertNotNull(resource);
        assertEquals(1L, resource.id());
        assertEquals(10L, resource.experienceId());
        assertEquals(LocalDateTime.of(2024, 7, 1, 9, 0), resource.startDateTime());
        assertEquals(LocalDateTime.of(2024, 7, 1, 17, 0), resource.endDateTime());
        assertEquals(20, resource.capacity());

        verify(entity).getId();
        verify(entity).getExperience();
        verify(experience).getId();
        verify(entity).getStartDateTime();
        verify(entity).getEndDateTime();
        verify(entity).getCapacity();
        verifyNoMoreInteractions(entity, experience);

    }

}
