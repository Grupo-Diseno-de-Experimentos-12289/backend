package pe.edu.upc.travelmatch.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.iam.domain.model.commands.UpdateUserProfileCommand;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.travelmatch.iam.domain.services.UserCommandService;
import pe.edu.upc.travelmatch.iam.domain.services.UserQueryService;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.UpdateUserProfileResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.UserResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

/** UsersController type. */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User Management Endpoints")
public class UsersController {
  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;

  /** Constructs a new UsersController. */
  public UsersController(UserQueryService userQueryService, UserCommandService userCommandService) {
    this.userQueryService = userQueryService;
    this.userCommandService = userCommandService;
  }

  /** Get all users. */
  @GetMapping
  public ResponseEntity<List<UserResource>> getAllUsers() {
    var getAllUsersQuery = new GetAllUsersQuery();
    var users = userQueryService.handle(getAllUsersQuery);
    var userResources =
        users.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
    return ResponseEntity.ok(userResources);
  }

  /** Get user by id. */
  @GetMapping(value = "/{userId}")
  public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
    var getUserByIdQuery = new GetUserByIdQuery(userId);
    var user = userQueryService.handle(getUserByIdQuery);
    if (user.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
    return ResponseEntity.ok(userResource);
  }

  /** Update user profile. */
  @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResource> updateUserProfile(
      @PathVariable Long userId, @RequestBody UpdateUserProfileResource resource) {
    var command =
        new UpdateUserProfileCommand(
            userId,
            resource.firstName(),
            resource.lastName(),
            resource.phone(),
            resource.profileType(),
            resource.avatarUrl());
    var updatedUser = userCommandService.handle(command);
    if (updatedUser.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(updatedUser.get());
    return ResponseEntity.ok(userResource);
  }
}
