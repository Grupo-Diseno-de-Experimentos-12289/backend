package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

import java.util.List;

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

    public Category() {}

    public Category(Categories name) {
        this.name = name;
    }

    public String getCategoryName() {
        return name.name();
    }

    public static Category getDefaultCategory() {
        return new Category(Categories.CULTURA);
    }

    public static Category toCategoryFromName(String category) {
        return new Category(Categories.valueOf(category));
    }

    public static List<Category> validateCategorySet(List<Category> categories) {
        if(categories.isEmpty()) {
            return List.of(getDefaultCategory());
        }
        return categories;
    }
}
