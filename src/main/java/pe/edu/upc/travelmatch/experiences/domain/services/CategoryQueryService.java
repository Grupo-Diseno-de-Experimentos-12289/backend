package pe.edu.upc.travelmatch.experiences.domain.services;
import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllCategoriesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetCategoryByIdQuery;
/**
 * Service to manage Category queries.
 */
public interface CategoryQueryService {
  /**
   * Retrieves all categories.
   *
   * @param query the get all categories query
   * @return the list of categories
   */
  List<Category> handle(GetAllCategoriesQuery query);
  /**
   * Handles the get category by id query.
   *
   * @param query the get category by id query
   * @return the matching category if found
   */
  Optional<Category> handle(GetCategoryByIdQuery query);
}
