package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.TicketTypeResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TicketTypeResourceFromEntityAssemblerTest {

  @Test
  @DisplayName("toResource should map TicketType entity to TicketTypeResource (AAA)")
  void toResourceFromEntity_ShouldMap() {
    // Arrange
    TicketType entity = mock(TicketType.class);
    when(entity.getId()).thenReturn(1L);
    when(entity.getTicketTypeName()).thenReturn("Type1");
    // Act
    TicketTypeResource resource =
        TicketTypeResourceFromEntityAssembler.toResourceFromEntity(entity);

    // Assert
    assertNotNull(resource);
    assertEquals(1L, resource.id());
    assertEquals("Type1", resource.name());

    verify(entity).getId();
    verify(entity).getTicketTypeName();
    verifyNoMoreInteractions(entity);
  }
}
