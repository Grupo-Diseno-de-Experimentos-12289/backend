package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CategoryResource;

class CategoryResourceFromEntityAssemblerTest {

  @Test
  @DisplayName("toResource should map Category entity to CategoryResource (AAA)")
  void toResource_ShouldMapEntityToResource() {
    // Arrange
    Category entity = mock(Category.class);
    when(entity.getId()).thenReturn(1L);
    when(entity.getCategoryName()).thenReturn("CULTURA");

    // Act
    CategoryResource resource = CategoryResourceFromEntityAssembler.toResourceFromEntity(entity);

    // Assert
    assertNotNull(resource);
    assertEquals(1L, resource.id());
    assertEquals("CULTURA", resource.name());
    verify(entity).getId();
    verify(entity).getCategoryName();
    verifyNoMoreInteractions(entity);
  }
}
