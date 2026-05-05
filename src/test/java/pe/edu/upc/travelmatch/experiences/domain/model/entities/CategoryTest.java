package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryTest {

    @Test
    void testCategoryConstructorAndGetters() {
        // Arrange
        Categories categoryName = Categories.CULTURA;

        // Act
        Category category = new Category(categoryName);

        // Assert
        assertEquals(categoryName, category.getName());
        assertEquals("CULTURA", category.getCategoryName());
        assertNull(category.getId()); // Should be null initially before persisting
    }

    @Test
    void testGetDefaultCategory() {
        // Act
        Category defaultCategory = Category.getDefaultCategory();

        // Assert
        assertEquals(Categories.CULTURA, defaultCategory.getName());
    }

    @Test
    void testToCategoryFromName() {
        // Arrange
        String name = "GASTRONOMIA";

        // Act
        Category category = Category.toCategoryFromName(name);

        // Assert
        assertEquals(Categories.GASTRONOMIA, category.getName());
    }

    @Test
    void testValidateCategorySetWhenEmpty() {
        // Act
        List<Category> categoryList = Category.validateCategorySet(List.of());

        // Assert
        assertEquals(1, categoryList.size());
        assertEquals(Categories.CULTURA, categoryList.getFirst().getName());
    }

    @Test
    void testValidateCategorySetWhenNotEmpty() {
        // Arrange
        List<Category> providedList = List.of(new Category(Categories.GASTRONOMIA));

        // Act
        List<Category> categoryList = Category.validateCategorySet(providedList);

        // Assert
        assertEquals(1, categoryList.size());
        assertEquals(Categories.GASTRONOMIA, categoryList.getFirst().getName());
    }
}
