package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllCategoriesQuery;
import pe.edu.upc.travelmatch.experiences.domain.services.CategoryQueryService;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CategoryResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.transform.CategoryResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Categories", description = "Category Management Endpoints")
public class CategoriesController {
    private final CategoryQueryService categoryQueryService;

    public CategoriesController(CategoryQueryService categoryQueryService) {
        this.categoryQueryService = categoryQueryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResource>> getAllCategories() {
        var getAllCategories = new GetAllCategoriesQuery();
        var categories = categoryQueryService.handle(getAllCategories);
        var categoryResources = categories.stream().map(CategoryResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(categoryResources);
    }
}
