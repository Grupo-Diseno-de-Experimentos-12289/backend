package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

/** Entity class for Category. */
@Entity
public class Category extends AuditableModel {
  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true, length = 50)
  private Categories name;

  /** Default constructor. */
  public Category() {}

  /**
   * Constructor with name.
   *
   * @param name the category name enum
   */
  public Category(Categories name) {
    this.name = name;
  }

  /**
   * Get the category name as string.
   *
   * @return the string name of the category
   */
  public String getCategoryName() {
    return name.name();
  }

  /**
   * Gets the default category.
   *
   * @return a default category
   */
  public static Category getDefaultCategory() {
    return new Category(Categories.CULTURA);
  }

  /**
   * Converts a string name to a Category.
   *
   * @param category the string name
   * @return the corresponding Category
   */
  public static Category toCategoryFromName(String category) {
    return new Category(Categories.valueOf(category));
  }

  /**
   * Validates a list of categories.
   *
   * @param categories the list to validate
   * @return the validated list or default if empty
   */
  public static List<Category> validateCategorySet(List<Category> categories) {
    if (categories.isEmpty()) {
      return List.of(getDefaultCategory());
    }
    return categories;
  }
}
