package pe.edu.upc.travelmatch.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.travelmatch.iam.domain.services.UserQueryService;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.UserResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

/**
 * REST controller for IAM users.
 */
@RestController
@RequestMapping(
    value = "/api/v1/users",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Users", description = "User Management Endpoints")
public final class UsersController {
  /**
   * User query service dependency.
   */
  private final UserQueryService userQueryService;

  /**
   * Constructor.
   *
   * @param userQueryServiceDependency user query service dependency
   */
  public UsersController(final UserQueryService userQueryServiceDependency) {
    this.userQueryService = userQueryServiceDependency;
  }

  /**
   * Returns all users.
   *
   * @return list of user resources
   */
  @GetMapping
  public ResponseEntity<List<UserResource>> getAllUsers() {
    var getAllUsersQuery = new GetAllUsersQuery();
    var users = userQueryService.handle(getAllUsersQuery);
    var userResources = users.stream()
        .map(UserResourceFromEntityAssembler::toResourceFromEntity)
        .toList();
    return ResponseEntity.ok(userResources);
  }

  /**
   * Returns a user by id.
   *
   * @param userId user id
   * @return user resource or 404 when not found
   */
  @GetMapping(value = "/{userId}")
  public ResponseEntity<UserResource> getUserById(
      @PathVariable final Long userId
  ) {
    var getUserByIdQuery = new GetUserByIdQuery(userId);
    var user = userQueryService.handle(getUserByIdQuery);
    if (user.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var userResource = UserResourceFromEntityAssembler
        .toResourceFromEntity(user.get());
    return ResponseEntity.ok(userResource);
  }
}
