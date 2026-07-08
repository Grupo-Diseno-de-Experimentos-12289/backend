package pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.iam.interfaces.acl.IamContextFacade;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

@ExtendWith(MockitoExtension.class)
class ExternalIamServiceTest {

  @Mock private IamContextFacade iamContextFacade;

  @InjectMocks private ExternalIamService externalIamService;

  @Test
  void testFetchUserIdByEmail_Found() {
    // Arrange
    String email = "test@example.com";
    Long expectedUserId = 1L;
    when(iamContextFacade.fetchUserIdByEmail(email)).thenReturn(expectedUserId);

    // Act
    Optional<UserId> result = externalIamService.fetchUserIdByEmail(email);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(expectedUserId, result.get().userId());
    verify(iamContextFacade, times(1)).fetchUserIdByEmail(email);
  }

  @Test
  void testFetchUserIdByEmail_NotFound() {
    // Arrange
    String email = "test@example.com";
    when(iamContextFacade.fetchUserIdByEmail(email)).thenReturn(0L);

    // Act
    Optional<UserId> result = externalIamService.fetchUserIdByEmail(email);

    // Assert
    assertTrue(result.isEmpty());
    verify(iamContextFacade, times(1)).fetchUserIdByEmail(email);
  }

  @Test
  void testExistsUserByEmailAndIdIsNot_True() {
    // Arrange
    String email = "test@example.com";
    Long id = 1L;
    when(iamContextFacade.existsUserByEmailAndIdIsNot(email, id)).thenReturn(true);

    // Act
    boolean result = externalIamService.existsUserByEmailAndIdIsNot(email, id);

    // Assert
    assertTrue(result);
    verify(iamContextFacade, times(1)).existsUserByEmailAndIdIsNot(email, id);
  }

  @Test
  void testExistsUserByEmailAndIdIsNot_False() {
    // Arrange
    String email = "test@example.com";
    Long id = 1L;
    when(iamContextFacade.existsUserByEmailAndIdIsNot(email, id)).thenReturn(false);

    // Act
    boolean result = externalIamService.existsUserByEmailAndIdIsNot(email, id);

    // Assert
    assertFalse(result);
    verify(iamContextFacade, times(1)).existsUserByEmailAndIdIsNot(email, id);
  }

  @Test
  void testExistsUserById_True() {
    // Arrange
    UserId userId = new UserId(1L);
    when(iamContextFacade.existsUserById(userId.userId())).thenReturn(true);

    // Act
    boolean result = externalIamService.existsUserById(userId);

    // Assert
    assertTrue(result);
    verify(iamContextFacade, times(1)).existsUserById(userId.userId());
  }

  @Test
  void testExistsUserById_False() {
    // Arrange
    UserId userId = new UserId(1L);
    when(iamContextFacade.existsUserById(userId.userId())).thenReturn(false);

    // Act
    boolean result = externalIamService.existsUserById(userId);

    // Assert
    assertFalse(result);
    verify(iamContextFacade, times(1)).existsUserById(userId.userId());
  }

  @Test
  void testCreateUser_Success() {
    // Arrange
    String email = "test@example.com";
    String password = "password";
    String firstName = "John";
    String lastName = "Doe";
    String phone = "123456789";
    List<String> roleNames = List.of("ROLE_USER");
    Long expectedUserId = 1L;

    when(iamContextFacade.createUser(email, password, firstName, lastName, phone, roleNames))
        .thenReturn(expectedUserId);

    // Act
    Optional<UserId> result =
        externalIamService.createUser(email, password, firstName, lastName, phone, roleNames);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(expectedUserId, result.get().userId());
    verify(iamContextFacade, times(1))
        .createUser(email, password, firstName, lastName, phone, roleNames);
  }

  @Test
  void testCreateUser_Failure() {
    // Arrange
    String email = "test@example.com";
    String password = "password";
    String firstName = "John";
    String lastName = "Doe";
    String phone = "123456789";
    List<String> roleNames = List.of("ROLE_USER");

    when(iamContextFacade.createUser(email, password, firstName, lastName, phone, roleNames))
        .thenReturn(0L);

    // Act
    Optional<UserId> result =
        externalIamService.createUser(email, password, firstName, lastName, phone, roleNames);

    // Assert
    assertTrue(result.isEmpty());
    verify(iamContextFacade, times(1))
        .createUser(email, password, firstName, lastName, phone, roleNames);
  }
}
