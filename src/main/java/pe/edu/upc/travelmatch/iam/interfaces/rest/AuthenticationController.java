package pe.edu.upc.travelmatch.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.iam.domain.services.UserCommandService;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.AuthenticatedUserResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignInResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignUpResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.UserResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import pe.edu.upc.travelmatch.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

/**
 * REST controller for IAM authentication operations.
 */
@RestController
@RequestMapping(
    value = "/api/v1/authentication",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public final class AuthenticationController {
  /**
   * User command service dependency.
   */
  private final UserCommandService userCommandService;

  /**
   * Constructor.
   *
   * @param userCommandServiceDependency user command service dependency
   */
  public AuthenticationController(
      final UserCommandService userCommandServiceDependency
  ) {
    this.userCommandService = userCommandServiceDependency;
  }

  /**
   * Signs a user in.
   *
   * @param signInResource sign-in data
   * @return authenticated user resource or 404 when authentication fails
   */
  @PostMapping("/sign-in")
  public ResponseEntity<AuthenticatedUserResource> signIn(
      @RequestBody final SignInResource signInResource
  ) {
    var signInCommand = SignInCommandFromResourceAssembler
        .toCommandFromResource(signInResource);
    var authenticatedUser = userCommandService.handle(signInCommand);
    if (authenticatedUser.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
        .toResourceFromEntity(
            authenticatedUser.get().getLeft(),
            authenticatedUser.get().getRight()
        );
    return ResponseEntity.ok(authenticatedUserResource);
  }

  /**
   * Signs a user up.
   *
   * @param signUpResource sign-up data
   * @return created user resource or 400 when creation fails
   */
  @PostMapping("/sign-up")
  public ResponseEntity<UserResource> signUp(
      @RequestBody final SignUpResource signUpResource
  ) {
    var signUpCommand = SignUpCommandFromResourceAssembler
        .toCommandFromResource(signUpResource);
    var user = userCommandService.handle(signUpCommand);
    if (user.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    var userResource = UserResourceFromEntityAssembler
        .toResourceFromEntity(user.get());
    return new ResponseEntity<>(userResource, HttpStatus.CREATED);
  }
}
