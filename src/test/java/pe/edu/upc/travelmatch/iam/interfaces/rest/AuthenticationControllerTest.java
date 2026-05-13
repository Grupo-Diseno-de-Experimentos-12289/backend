package pe.edu.upc.travelmatch.iam.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.travelmatch.iam.domain.services.UserCommandService;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.AuthenticatedUserResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignInResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignUpResource;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.UserResource;

/**
 * Unit tests for {@link AuthenticationController}.
 *
 * <p>This class validates the authentication endpoints, including sign-in and sign-up, ensuring
 * correct HTTP responses and resource mapping.
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  @Mock private UserCommandService userCommandService;
  @InjectMocks private AuthenticationController authenticationController;

  @Test
  @DisplayName("signIn should return 200 OK and AuthenticatedUserResource")
  void signIn_ShouldReturnOkAndResource_WhenSuccessful() {
    // Arrange
    var signInResource = new SignInResource("john.doe@example.com", "password");
    var userSpy = spy(new User("john.doe@example.com", "encoded", "John", "Doe", "123"));
    when(userSpy.getId()).thenReturn(1L);
    var token = "jwt-token";
    var pair = new ImmutablePair<>(userSpy, token);

    when(userCommandService.handle(any(SignInCommand.class))).thenReturn(Optional.of(pair));

    // Act
    ResponseEntity<AuthenticatedUserResource> response =
        authenticationController.signIn(signInResource);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("jwt-token", response.getBody().token());

    verify(userCommandService).handle(any(SignInCommand.class));
    verifyNoMoreInteractions(userCommandService);
  }

  @Test
  @DisplayName("signIn should return 404 Not Found when user does not exist or invalid")
  void signIn_ShouldReturnNotFound_WhenFailed() {
    // Arrange
    var signInResource = new SignInResource("john.doe@example.com", "password");

    when(userCommandService.handle(any(SignInCommand.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<AuthenticatedUserResource> response =
        authenticationController.signIn(signInResource);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());

    verify(userCommandService).handle(any(SignInCommand.class));
    verifyNoMoreInteractions(userCommandService);
  }

  @Test
  @DisplayName("signUp should return 201 Created and UserResource")
  void signUp_ShouldReturnCreatedAndResource_WhenSuccessful() {
    // Arrange
    var signUpResource =
        new SignUpResource(
            "john.doe@example.com", "password", "John", "Doe", "123", new ArrayList<>());
    var userSpy = spy(new User("john.doe@example.com", "encoded", "John", "Doe", "123"));
    when(userSpy.getId()).thenReturn(1L);

    when(userCommandService.handle(any(SignUpCommand.class))).thenReturn(Optional.of(userSpy));

    // Act
    ResponseEntity<UserResource> response = authenticationController.signUp(signUpResource);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("John", response.getBody().firstName());

    verify(userCommandService).handle(any(SignUpCommand.class));
    verifyNoMoreInteractions(userCommandService);
  }

  @Test
  @DisplayName("signUp should return 400 Bad Request when failed")
  void signUp_ShouldReturnBadRequest_WhenFailed() {
    // Arrange
    var signUpResource =
        new SignUpResource(
            "john.doe@example.com", "password", "John", "Doe", "123", new ArrayList<>());

    when(userCommandService.handle(any(SignUpCommand.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<UserResource> response = authenticationController.signUp(signUpResource);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());

    verify(userCommandService).handle(any(SignUpCommand.class));
    verifyNoMoreInteractions(userCommandService);
  }
}
