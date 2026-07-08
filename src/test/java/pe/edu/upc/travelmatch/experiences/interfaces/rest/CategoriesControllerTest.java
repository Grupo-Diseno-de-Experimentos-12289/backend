package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllCategoriesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.services.CategoryQueryService;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CategoryResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriesControllerTest {

  @Mock private CategoryQueryService categoryQueryService;

  @InjectMocks private CategoriesController categoriesController;

  @Test
  void testGetAllCategories_Ok() {
    // Arrange
    Category category = new Category(Categories.CULTURA);
    List<Category> categoriesList = List.of(category);

    when(categoryQueryService.handle(any(GetAllCategoriesQuery.class))).thenReturn(categoriesList);

    // Act
    ResponseEntity<List<CategoryResource>> response = categoriesController.getAllCategories();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("CULTURA", response.getBody().get(0).name());
  }

  @Test
  void testGetAllCategories_EmptyList() {
    // Arrange
    when(categoryQueryService.handle(any(GetAllCategoriesQuery.class))).thenReturn(List.of());

    // Act
    ResponseEntity<List<CategoryResource>> response = categoriesController.getAllCategories();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
  }
}
