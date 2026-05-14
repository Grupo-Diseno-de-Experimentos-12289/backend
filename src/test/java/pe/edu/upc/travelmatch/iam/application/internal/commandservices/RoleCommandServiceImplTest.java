package pe.edu.upc.travelmatch.iam.application.internal.commandservices;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SeedRolesCommand;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

/**
 * Unit tests for {@link RoleCommandServiceImpl}.
 *
 * <p>This class validates the behavior of the RoleCommandServiceImpl, specifically the seeding of
 * initial roles into the repository.
 */
@ExtendWith(MockitoExtension.class)
class RoleCommandServiceImplTest {

  @Mock private RoleRepository roleRepository;
  @InjectMocks private RoleCommandServiceImpl roleCommandService;

  @Test
  @DisplayName("handle(SeedRolesCommand) should save all missing roles")
  void handle_SeedRolesCommand_ShouldSaveMissingRoles() {
    // Arrange
    final var command = new SeedRolesCommand();

    when(roleRepository.existsByName(Roles.ROLE_ADMIN)).thenReturn(true);
    when(roleRepository.existsByName(Roles.ROLE_AGENCY_STAFF)).thenReturn(false);
    when(roleRepository.existsByName(Roles.ROLE_TOURIST)).thenReturn(false);

    // Act
    roleCommandService.handle(command);

    // Assert
    verify(roleRepository, atLeastOnce()).existsByName(any());
    verify(roleRepository, atLeastOnce()).save(any(Role.class));
  }
}
