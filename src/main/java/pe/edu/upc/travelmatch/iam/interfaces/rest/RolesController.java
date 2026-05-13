package pe.edu.upc.travelmatch.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllRolesQuery;
import pe.edu.upc.travelmatch.iam.domain.services.RoleQueryService;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.RoleResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.transform.RoleResourceFromEntityAssembler;

/**
 * REST controller for IAM roles.
 */
@RestController
@RequestMapping(
    value = "/api/v1/roles",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Roles", description = "Role Management Endpoints")
public final class RolesController {
  /**
   * Role query service dependency.
   */
  private final RoleQueryService roleQueryService;

  /**
   * Constructor.
   *
   * @param roleQueryServiceDependency role query service dependency
   */
  public RolesController(final RoleQueryService roleQueryServiceDependency) {
    this.roleQueryService = roleQueryServiceDependency;
  }

  /**
   * Returns all roles.
   *
   * @return list of role resources
   */
  @GetMapping
  public ResponseEntity<List<RoleResource>> getAllRoles() {
    var getAllRolesQuery = new GetAllRolesQuery();
    var roles = roleQueryService.handle(getAllRolesQuery);
    var roleResources = roles.stream()
        .map(RoleResourceFromEntityAssembler::toResourceFromEntity)
        .toList();
    return ResponseEntity.ok(roleResources);
  }
}
