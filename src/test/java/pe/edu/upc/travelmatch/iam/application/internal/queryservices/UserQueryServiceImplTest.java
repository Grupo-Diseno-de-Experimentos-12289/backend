package pe.edu.upc.travelmatch.iam.application.internal.queryservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.UserRepository;

/**
 * Unit tests for {@link UserQueryServiceImpl}.
 *
 * <p>This class validates the behavior of the UserQueryServiceImpl, including fetching users by ID,
 * email, or all users.
 */
@ExtendWith(MockitoExtension.class)
class UserQueryServiceImplTest {

  @Mock private UserRepository userRepository;
  @InjectMocks private UserQueryServiceImpl userQueryService;

  @Test
  @DisplayName("handle(GetAllUsersQuery) should return a list of users")
  void handle_GetAllUsersQuery_ShouldReturnListOfUsers() {
    // Arrange
    var query = new GetAllUsersQuery();
    var mockUser = mock(User.class);
    var expectedUsers = List.of(mockUser);

    when(userRepository.findAll()).thenReturn(expectedUsers);

    // Act
    var result = userQueryService.handle(query);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(expectedUsers, result);

    verify(userRepository).findAll();
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  @DisplayName("handle(GetUserByIdQuery) should return an Optional of user when found")
  void handle_GetUserByIdQuery_ShouldReturnOptionalUser() {
    // Arrange
    var query = new GetUserByIdQuery(1L);
    var mockUser = mock(User.class);

    when(userRepository.findById(query.userId())).thenReturn(Optional.of(mockUser));

    // Act
    var result = userQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(mockUser, result.get());

    verify(userRepository).findById(query.userId());
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  @DisplayName("handle(GetUserByEmailQuery) should return an Optional of user when found")
  void handle_GetUserByEmailQuery_ShouldReturnOptionalUser() {
    // Arrange
    var query = new GetUserByEmailQuery("john.doe@example.com");
    var mockUser = mock(User.class);

    when(userRepository.findByEmail(query.email())).thenReturn(Optional.of(mockUser));

    // Act
    var result = userQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(mockUser, result.get());

    verify(userRepository).findByEmail(query.email());
    verifyNoMoreInteractions(userRepository);
  }
}
