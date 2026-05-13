package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;

public class ExperienceResourceFromEntityAssemblerTest {

  @Test
  @DisplayName("toResource should map Experience entity to ExperienceResource (AAA)")
  void toResourceFromEntity_ShouldMap() {
    // Arrange
    Experience entity = mock(Experience.class);
    AgencyId agencyId = mock(AgencyId.class);
    Category category = mock(Category.class);
    DestinationId destinationId = mock(DestinationId.class);

    when(entity.getId()).thenReturn(1L);
    when(entity.getTitle()).thenReturn("title");
    when(entity.getDescription()).thenReturn("Descripcion");
    when(entity.getAgencyId()).thenReturn(agencyId);
    when(agencyId.value()).thenReturn(10L);
    when(entity.getCategory()).thenReturn(category);
    when(category.getId()).thenReturn(10L);
    when(entity.getDestinationId()).thenReturn(destinationId);
    when(destinationId.value()).thenReturn(2L);
    when(entity.getDuration()).thenReturn("2 days");
    when(entity.getMeetingPoint()).thenReturn("Meeting Point");

    // Act
    var resource = ExperienceResourceFromEntityAssembler.toResourceFromEntity(entity);

    // Assert
    assertNotNull(resource);
    assertEquals(1L, resource.id());
    assertEquals("title", resource.title());
    assertEquals("Descripcion", resource.description());
    assertEquals(10L, resource.agencyId());
    assertEquals(category, resource.category());
    assertEquals(2L, resource.destinationId());
    assertEquals("2 days", resource.duration());
    assertEquals("Meeting Point", resource.meetingPoint());

    verify(entity).getId();
    verify(entity).getTitle();
    verify(entity).getDescription();
    verify(entity).getAgencyId();
    verify(agencyId).value();
    verify(entity).getCategory();
    verify(entity).getDestinationId();
    verify(entity).getMeetingPoint();
    verify(destinationId).value();
    verify(entity).getDuration();
    verifyNoMoreInteractions(entity, agencyId, category, destinationId);
  }
}
