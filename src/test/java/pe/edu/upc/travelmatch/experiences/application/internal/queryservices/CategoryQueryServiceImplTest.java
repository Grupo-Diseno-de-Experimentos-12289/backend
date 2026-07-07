package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllCategoriesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetCategoryByIdQuery;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryQueryServiceImplTest {

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private CategoryQueryServiceImpl categoryQueryService;

  @Test
  void handle_getAllCategories_retornaListaDeCategorias() {

    // Arrange
    GetAllCategoriesQuery query = new GetAllCategoriesQuery();
    Category c1 = new Category();
    Category c2 = new Category();
    when(categoryRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

    // Act
    List<Category> result = categoryQueryService.handle(query);

    // Assert
    assertEquals(2, result.size());
    verify(categoryRepository, times(1)).findAll();
  }

  @Test
  void handle_getCategoryById_retornaCategoria() {
    // Arrange
    Long id = 1L;
    GetCategoryByIdQuery query = new GetCategoryByIdQuery(id);
    Category category = new Category();

    when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

    // Act
    Optional<Category> result = categoryQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    verify(categoryRepository, times(1)).findById(id);
  }
}
