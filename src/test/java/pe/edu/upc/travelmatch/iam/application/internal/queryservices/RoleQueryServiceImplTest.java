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
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllRolesQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetRoleByIdQuery;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

/**
 * Unit tests for {@link RoleQueryServiceImpl}.
 *
 * <p>This class validates the behavior of the RoleQueryServiceImpl, including fetching all roles or
 * a specific role by ID.
 */
@ExtendWith(MockitoExtension.class)
class RoleQueryServiceImplTest {

  @Mock private RoleRepository roleRepository;
  @InjectMocks private RoleQueryServiceImpl roleQueryService;

  @Test
  @DisplayName("handle(GetAllRolesQuery) should return a list of roles")
  void handle_GetAllRolesQuery_ShouldReturnListOfRoles() {
    // Arrange
    var query = new GetAllRolesQuery();
    var mockRole = mock(Role.class);
    var expectedRoles = List.of(mockRole);

    when(roleRepository.findAll()).thenReturn(expectedRoles);

    // Act
    var result = roleQueryService.handle(query);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(expectedRoles, result);

    verify(roleRepository).findAll();
    verifyNoMoreInteractions(roleRepository);
  }

  @Test
  @DisplayName("handle(GetRoleByIdQuery) should return an Optional of role when found")
  void handle_GetRoleByIdQuery_ShouldReturnOptionalRole() {
    // Arrange
    var query = new GetRoleByIdQuery(1L);
    var mockRole = mock(Role.class);

    when(roleRepository.findById(query.roleId())).thenReturn(Optional.of(mockRole));

    // Act
    var result = roleQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(mockRole, result.get());

    verify(roleRepository).findById(query.roleId());
    verifyNoMoreInteractions(roleRepository);
  }
}
