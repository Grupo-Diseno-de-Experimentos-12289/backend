package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.ExperienceMediaResource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ExperienceMediaResourceFromEntityAssemblerTest {

    @Test
    @DisplayName("toResource should map ExperienceMedia entity to ExperienceMediaResource (AAA)")
    void toResourceFromEntity_ShouldMap() {
        //Arrange
        ExperienceMedia entity = mock(ExperienceMedia.class);
        Experience experience = mock(Experience.class);
        when(entity.getId()).thenReturn(1L);
        when(entity.getExperience()).thenReturn(experience);
        when(experience.getId()).thenReturn(10L);
        when(entity.getMediaUrl()).thenReturn("http://example.com/media.jpg");
        when(entity.getCaption()).thenReturn("image/jpeg");

        //Act
        ExperienceMediaResource resource = ExperienceMediaResourceFromEntityAssembler.toResourceFromEntity(entity);

        //Assert
        assertNotNull(resource);
        assertEquals(1L, resource.id());
        assertEquals(10L, resource.experienceId());
        assertEquals("http://example.com/media.jpg", resource.mediaUrl());
        assertEquals("image/jpeg", resource.caption());

        verify(entity).getId();
        verify(entity).getExperience();
        verify(experience).getId();
        verify(entity).getMediaUrl();
        verify(entity).getCaption();
        verifyNoMoreInteractions(entity, experience);
    }
    
}
