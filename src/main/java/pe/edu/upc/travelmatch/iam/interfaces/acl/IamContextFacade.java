package pe.edu.upc.travelmatch.iam.interfaces.acl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.travelmatch.iam.domain.services.UserCommandService;
import pe.edu.upc.travelmatch.iam.domain.services.UserQueryService;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.UserResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

/**
 * Facade for cross-context IAM operations.
 */
@Service
public final class IamContextFacade {
  /**
   * User command service dependency.
   */
  private final UserCommandService userCommandService;
  /**
   * User query service dependency.
   */
  private final UserQueryService userQueryService;

  /**
   * Constructor.
   *
   * @param userCommandServiceDependency command service dependency
   * @param userQueryServiceDependency query service dependency
   */
  public IamContextFacade(
      final UserCommandService userCommandServiceDependency,
      final UserQueryService userQueryServiceDependency
  ) {
    this.userCommandService = userCommandServiceDependency;
    this.userQueryService = userQueryServiceDependency;
  }

  /**
   * Creates a user with the default tourist role.
   *
   * @param email user email
   * @param password user password
   * @param firstName user first name
   * @param lastName user last name
   * @param phone user phone
   * @return created user id, or 0 when creation fails
   */
  public Long createUser(
      final String email,
      final String password,
      final String firstName,
      final String lastName,
      final String phone
  ) {
    var defaultRole = Role.toRoleFromName("ROLE_TOURIST");
    var signUpCommand = new SignUpCommand(
        email,
        password,
        firstName,
        lastName,
        phone,
        List.of(defaultRole)
    );
    var result = userCommandService.handle(signUpCommand);
    if (result.isEmpty()) {
      return 0L;
    }
    return result.get().getId();
  }

  /**
   * Creates a user with the provided roles.
   *
   * @param email user email
   * @param password user password
   * @param firstName user first name
   * @param lastName user last name
   * @param phone user phone
   * @param roleNames role names to assign
   * @return created user id, or 0 when creation fails
   */
  public Long createUser(
      final String email,
      final String password,
      final String firstName,
      final String lastName,
      final String phone,
      final List<String> roleNames
  ) {
    if (roleNames == null) {
      return createUser(email, password, firstName, lastName, phone);
    }
    var roles = roleNames.stream()
        .map(Role::toRoleFromName)
        .toList();
    var signUpCommand = new SignUpCommand(
        email,
        password,
        firstName,
        lastName,
        phone,
        roles
    );
    var result = userCommandService.handle(signUpCommand);
    if (result.isEmpty()) {
      return 0L;
    }
    return result.get().getId();
  }

  /**
   * Fetches a user by id.
   *
   * @param userId user id
   * @return optional user resource
   */
  public Optional<UserResource> fetchUserById(final Long userId) {
    var getUserByIdQuery = new GetUserByIdQuery(userId);
    var result = userQueryService.handle(getUserByIdQuery);
    if (result.isEmpty()) {
      return Optional.empty();
    }
    var userResource = UserResourceFromEntityAssembler
        .toResourceFromEntity(result.get());
    return Optional.of(userResource);
  }

  /**
   * Fetches a user id by email.
   *
   * @param email user email
   * @return user id, or 0 when not found
   */
  public Long fetchUserIdByEmail(final String email) {
    var getUserByEmailQuery = new GetUserByEmailQuery(email);
    var result = userQueryService.handle(getUserByEmailQuery);
    if (result.isEmpty()) {
      return 0L;
    }
    return result.get().getId();
  }

  /**
   * Checks whether the email belongs to another user.
   *
   * @param email user email
   * @param id user id to exclude
   * @return true when another user owns the email
   */
  public boolean existsUserByEmailAndIdIsNot(
      final String email,
      final Long id
  ) {
    var getUserByEmailQuery = new GetUserByEmailQuery(email);
    var result = userQueryService.handle(getUserByEmailQuery);
    if (result.isEmpty()) {
      return false;
    }
    return !Objects.equals(result.get().getId(), id);
  }

  /**
   * Checks whether a user exists by id.
   *
   * @param id user id
   * @return true when the user exists
   */
  public boolean existsUserById(final Long id) {
    var getUserByIdQuery = new GetUserByIdQuery(id);
    var result = userQueryService.handle(getUserByIdQuery);
    return result.isPresent();
  }

  /**
   * Fetches the email for a user id.
   *
   * @param userId user id
   * @return the email, or an empty string when not found
   */
  public String fetchEmailByUserId(final Long userId) {
    var getUserByIdQuery = new GetUserByIdQuery(userId);
    var result = userQueryService.handle(getUserByIdQuery);
    if (result.isEmpty()) {
      return Strings.EMPTY;
    }
    return result.get().getEmail();
  }

  /**
   * Checks whether the user owns a given role.
   *
   * @param userId user id
   * @param roleName role name
   * @return true when the user has the role
   */
  public boolean existsUserByRole(final Long userId, final String roleName) {
    var query = new GetUserByIdQuery(userId);
    var result = userQueryService.handle(query);
    if (result.isEmpty()) {
      return false;
    }

    return result.get().getRoles().stream()
        .anyMatch(role -> role.getStringName().equals(roleName));
  }
}
