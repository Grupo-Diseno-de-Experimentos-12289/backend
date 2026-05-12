package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import java.util.Arrays;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedCategoriesCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.services.CategoryCommandService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.CategoryRepository;

/**
 * Service implementation for managing Category commands.
 */
@Service
public class CategoryCommandServiceImpl implements CategoryCommandService {

  private final CategoryRepository categoryRepository;

  /**
   * Constructs the CategoryCommandServiceImpl.
   *
   * @param categoryRepository the category repository
   */
  public CategoryCommandServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void handle(SeedCategoriesCommand command) {
    Arrays.stream(Categories.values()).forEach(category -> {
      if (!categoryRepository.existsByName(category)) {
        categoryRepository.save(new Category(Categories.valueOf(category.name())));
      }
    });
  }
}
