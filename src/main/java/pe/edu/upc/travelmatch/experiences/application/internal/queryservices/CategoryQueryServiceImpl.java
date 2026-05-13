package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllCategoriesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetCategoryByIdQuery;
import pe.edu.upc.travelmatch.experiences.domain.services.CategoryQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.CategoryRepository;

/**
 * Service implementation for managing Category queries.
 */
@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {

  private final CategoryRepository categoryRepository;

  /**
   * Constructs a CategoryQueryServiceImpl.
   *
   * @param categoryRepository the category repository
   */
  public CategoryQueryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<Category> handle(GetAllCategoriesQuery query) {
    return categoryRepository.findAll();
  }

  @Override
  public Optional<Category> handle(GetCategoryByIdQuery query) {
    return categoryRepository.findById(query.categoryId());
  }
}
