package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CategoryTest {

  @Test
  void testCategoryConstructorAndGetters() {
    // Arrange
    Categories expectedName = Categories.CULTURA;

    // Act
    Category category = new Category(expectedName);

    // Assert
    assertEquals(expectedName, category.getName());
  }

  @Test
  void testGetCategoryName_ReturnsStringRepresentation() {
    // Arrange
    Category category = new Category(Categories.GASTRONOMIA);

    // Act
    String name = category.getCategoryName();

    // Assert
    assertEquals("GASTRONOMIA", name);
  }

  @Test
  void testGetDefaultCategory_ReturnsCulturaCategory() {
    // Act
    Category defaultCategory = Category.getDefaultCategory();

    // Assert
    assertNotNull(defaultCategory);
    assertEquals(Categories.CULTURA, defaultCategory.getName());
  }

  @Test
  void testToCategoryFromName_ReturnsCategoryWithCorrectEnum() {
    // Arrange
    String categoryName = "NATURALEZA";

    // Act
    Category result = Category.toCategoryFromName(categoryName);

    // Assert
    assertNotNull(result);
    assertEquals(Categories.NATURALEZA, result.getName());
  }

  @Test
  void testValidateCategorySet_ReturnsDefaultWhenListIsEmpty() {
    // Arrange
    List<Category> emptyList = Collections.emptyList();

    // Act
    List<Category> result = Category.validateCategorySet(emptyList);

    // Assert
    assertEquals(1, result.size());
    assertEquals(Categories.CULTURA, result.get(0).getName());
  }

  @Test
  void testValidateCategorySet_ReturnsSameListWhenNotEmpty() {
    // Arrange
    Category aventura = new Category(Categories.GASTRONOMIA);
    List<Category> categoryList = List.of(aventura);

    // Act
    List<Category> result = Category.validateCategorySet(categoryList);

    // Assert
    assertEquals(1, result.size());
    assertEquals(aventura, result.get(0));
  }
}
