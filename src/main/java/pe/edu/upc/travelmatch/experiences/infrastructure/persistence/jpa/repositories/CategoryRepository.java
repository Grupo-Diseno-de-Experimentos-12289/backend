package pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;

/** Repository interface for managing Category entities. */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  /**
   * Finds a Category using its enumeration format context.
   *
   * @param name the categories ENUM type
   * @return optional object matched
   */
  Optional<Category> findByName(Categories name);

  /**
   * Checks whether the category exists.
   *
   * @param name the enum representing the category
   * @return true if exists, false otherwise
   */
  boolean existsByName(Categories name);
}
