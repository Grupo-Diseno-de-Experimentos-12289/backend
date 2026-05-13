package pe.edu.upc.travelmatch.iam.interfaces.acl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.travelmatch.iam.domain.services.UserCommandService;
import pe.edu.upc.travelmatch.iam.domain.services.UserQueryService;

/**
 * Test class for {@link IamContextFacade}. Validates the anti-corruption layer that exposes IAM
 * capabilities to other bounded contexts.
 */
@ExtendWith(MockitoExtension.class)
class IamContextFacadeTest {

  @Mock private UserCommandService userCommandService;
  @Mock private UserQueryService userQueryService;

  @InjectMocks private IamContextFacade iamContextFacade;

  @Test
  @DisplayName("createUser should return 0L when command service returns empty")
  void createUser_ShouldReturnZero_WhenEmpty() {
    // Arrange
    when(userCommandService.handle(any(SignUpCommand.class))).thenReturn(Optional.empty());

    // Act
    Long result =
        iamContextFacade.createUser(
            "user@test.com", "pass", "John", "Doe", "999", List.of("ROLE_TOURIST"));

    // Assert
    assertEquals(0L, result);
    verify(userCommandService).handle(any(SignUpCommand.class));
    verifyNoMoreInteractions(userCommandService, userQueryService);
  }

  @Test
  @DisplayName("createUser should return user ID when command service returns a user")
  void createUser_ShouldReturnUserId_WhenPresent() {
    // Arrange
    var user = mock(User.class);
    when(user.getId()).thenReturn(10L);
    when(userCommandService.handle(any(SignUpCommand.class))).thenReturn(Optional.of(user));

    // Act
    Long result =
        iamContextFacade.createUser(
            "user@test.com", "pass", "John", "Doe", "999", List.of("ROLE_TOURIST"));

    // Assert
    assertEquals(10L, result);
    verify(userCommandService).handle(any(SignUpCommand.class));
    verifyNoMoreInteractions(userCommandService, userQueryService);
  }

  @Test
  @DisplayName("fetchUserById should return empty when user is not found")
  void fetchUserById_ShouldReturnEmpty_WhenNotFound() {
    // Arrange
    when(userQueryService.handle(new GetUserByIdQuery(99L))).thenReturn(Optional.empty());

    // Act
    var result = iamContextFacade.fetchUserById(99L);

    // Assert
    assertTrue(result.isEmpty());
    verify(userQueryService).handle(new GetUserByIdQuery(99L));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("fetchUserById should return UserResource when user is found")
  void fetchUserById_ShouldReturnResource_WhenFound() {
    // Arrange
    var user = mock(User.class);
    when(user.getId()).thenReturn(1L);
    when(user.getEmail()).thenReturn("john@test.com");
    when(user.getFirstName()).thenReturn("John");
    when(user.getLastName()).thenReturn("Doe");
    when(user.getPhone()).thenReturn("999");
    when(user.getRoles()).thenReturn(Set.of());
    when(userQueryService.handle(new GetUserByIdQuery(1L))).thenReturn(Optional.of(user));

    // Act
    var result = iamContextFacade.fetchUserById(1L);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(1L, result.get().id());
    assertEquals("john@test.com", result.get().email());
    verify(userQueryService).handle(new GetUserByIdQuery(1L));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("fetchUserIdByEmail should return 0L when user is not found")
  void fetchUserIdByEmail_ShouldReturnZero_WhenNotFound() {
    // Arrange
    when(userQueryService.handle(new GetUserByEmailQuery("notfound@test.com")))
        .thenReturn(Optional.empty());

    // Act
    Long result = iamContextFacade.fetchUserIdByEmail("notfound@test.com");

    // Assert
    assertEquals(0L, result);
    verify(userQueryService).handle(new GetUserByEmailQuery("notfound@test.com"));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("fetchUserIdByEmail should return user ID when user is found")
  void fetchUserIdByEmail_ShouldReturnId_WhenFound() {
    // Arrange
    var user = mock(User.class);
    when(user.getId()).thenReturn(5L);
    when(userQueryService.handle(new GetUserByEmailQuery("found@test.com")))
        .thenReturn(Optional.of(user));

    // Act
    Long result = iamContextFacade.fetchUserIdByEmail("found@test.com");

    // Assert
    assertEquals(5L, result);
    verify(userQueryService).handle(new GetUserByEmailQuery("found@test.com"));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("existsUserByEmailAndIdIsNot should return false when no user found with email")
  void existsUserByEmailAndIdIsNot_ShouldReturnFalse_WhenNotFound() {
    // Arrange
    when(userQueryService.handle(new GetUserByEmailQuery("x@test.com")))
        .thenReturn(Optional.empty());

    // Act
    boolean result = iamContextFacade.existsUserByEmailAndIdIsNot("x@test.com", 1L);

    // Assert
    assertFalse(result);
    verify(userQueryService).handle(new GetUserByEmailQuery("x@test.com"));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("existsUserByEmailAndIdIsNot should return false when found user has the same ID")
  void existsUserByEmailAndIdIsNot_ShouldReturnFalse_WhenSameId() {
    // Arrange
    var user = mock(User.class);
    when(user.getId()).thenReturn(7L);
    when(userQueryService.handle(new GetUserByEmailQuery("same@test.com")))
        .thenReturn(Optional.of(user));

    // Act
    boolean result = iamContextFacade.existsUserByEmailAndIdIsNot("same@test.com", 7L);

    // Assert
    assertFalse(result);
    verify(userQueryService).handle(new GetUserByEmailQuery("same@test.com"));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("existsUserByEmailAndIdIsNot should return true when found user has a different ID")
  void existsUserByEmailAndIdIsNot_ShouldReturnTrue_WhenDifferentId() {
    // Arrange
    var user = mock(User.class);
    when(user.getId()).thenReturn(8L);
    when(userQueryService.handle(new GetUserByEmailQuery("diff@test.com")))
        .thenReturn(Optional.of(user));

    // Act
    boolean result = iamContextFacade.existsUserByEmailAndIdIsNot("diff@test.com", 99L);

    // Assert
    assertTrue(result);
    verify(userQueryService).handle(new GetUserByEmailQuery("diff@test.com"));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("existsUserById should return true when user is found")
  void existsUserById_ShouldReturnTrue_WhenFound() {
    // Arrange
    var user = mock(User.class);
    when(userQueryService.handle(new GetUserByIdQuery(1L))).thenReturn(Optional.of(user));

    // Act
    boolean result = iamContextFacade.existsUserById(1L);

    // Assert
    assertTrue(result);
    verify(userQueryService).handle(new GetUserByIdQuery(1L));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("fetchEmailByUserId should return empty string when user is not found")
  void fetchEmailByUserId_ShouldReturnEmpty_WhenNotFound() {
    // Arrange
    when(userQueryService.handle(new GetUserByIdQuery(99L))).thenReturn(Optional.empty());

    // Act
    String result = iamContextFacade.fetchEmailByUserId(99L);

    // Assert
    assertTrue(result.isBlank());
    verify(userQueryService).handle(new GetUserByIdQuery(99L));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("fetchEmailByUserId should return email when user is found")
  void fetchEmailByUserId_ShouldReturnEmail_WhenFound() {
    // Arrange
    var user = mock(User.class);
    when(user.getEmail()).thenReturn("user@test.com");
    when(userQueryService.handle(new GetUserByIdQuery(1L))).thenReturn(Optional.of(user));

    // Act
    String result = iamContextFacade.fetchEmailByUserId(1L);

    // Assert
    assertEquals("user@test.com", result);
    verify(userQueryService).handle(new GetUserByIdQuery(1L));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("existsUserByRole should return true when user has the specified role")
  void existsUserByRole_ShouldReturnTrue_WhenRoleMatches() {
    // Arrange
    var role = mock(Role.class);
    var user = mock(User.class);
    when(role.getStringName()).thenReturn("ROLE_TOURIST");
    when(user.getRoles()).thenReturn(Set.of(role));
    when(userQueryService.handle(new GetUserByIdQuery(1L))).thenReturn(Optional.of(user));

    // Act
    boolean result = iamContextFacade.existsUserByRole(1L, "ROLE_TOURIST");

    // Assert
    assertTrue(result);
    verify(userQueryService).handle(new GetUserByIdQuery(1L));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }

  @Test
  @DisplayName("existsUserByRole should return false when user does not have the specified role")
  void existsUserByRole_ShouldReturnFalse_WhenRoleDoesNotMatch() {
    // Arrange
    var role = mock(Role.class);
    var user = mock(User.class);
    when(role.getStringName()).thenReturn("ROLE_ADMIN");
    when(user.getRoles()).thenReturn(Set.of(role));
    when(userQueryService.handle(new GetUserByIdQuery(1L))).thenReturn(Optional.of(user));

    // Act
    boolean result = iamContextFacade.existsUserByRole(1L, "ROLE_TOURIST");

    // Assert
    assertFalse(result);
    verify(userQueryService).handle(new GetUserByIdQuery(1L));
    verifyNoMoreInteractions(userQueryService, userCommandService);
  }
}
