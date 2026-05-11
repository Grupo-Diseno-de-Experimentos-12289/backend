package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedCategoriesCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.CategoryRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryCommandServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryCommandServiceImpl categoryCommandService;

    @Test
    void handle_pueblaCategoriasSiNoExisten() {
        // Arrange
        SeedCategoriesCommand command = new SeedCategoriesCommand();
        // Simulamos que no existe ninguna categoría para que proceda con el guardado
        when(categoryRepository.existsByName(any(Categories.class))).thenReturn(false);

        // Act
        categoryCommandService.handle(command);

        // Assert
        // Debe guardar cuantas enum de Categorias existan
        int expectedInvocations = Categories.values().length;
        verify(categoryRepository, times(expectedInvocations)).save(any(Category.class));
    }

    @Test
    void handle_noGuardaCategoriasSiYaExisten() {
        // Arrange
        SeedCategoriesCommand command = new SeedCategoriesCommand();
        // Simulamos que ya existen
        when(categoryRepository.existsByName(any(Categories.class))).thenReturn(true);

        // Act
        categoryCommandService.handle(command);

        // Assert
        verify(categoryRepository, never()).save(any(Category.class));
    }
}

