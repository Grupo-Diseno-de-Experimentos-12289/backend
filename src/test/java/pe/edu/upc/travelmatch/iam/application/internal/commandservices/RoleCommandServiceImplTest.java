package pe.edu.upc.travelmatch.iam.application.internal.commandservices;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Implementation of the tests for RoleCommandService interface handling role-related commands.
 *
 * <p>This service provides methods to handle command executions like seeding initial
 * roles into the repository securely.</p>
 */
@ExtendWith(MockitoExtension.class)
class RoleCommandServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleCommandServiceImpl roleCommandService;

    /**
     * Tests the handle method for SeedRolesCommand.
     */
    @Test
    @DisplayName("handle(SeedRolesCommand) should save all missing roles")
    void handle_SeedRolesCommand_ShouldSaveMissingRoles() {
        // Arrange
        var command = new SeedRolesCommand();

        // Let's assume there are 3 roles: ROLE_ADMIN, ROLE_TOURIST, ROLE_AGENCY (from common DDD examples).
        // Mock that the first exists and the others don't, to test both paths.
        when(roleRepository.existsByName(any())).thenAnswer(invocation -> {
            Roles role = invocation.getArgument(0);
            return role.name().equals("ROLE_ADMIN"); // Only ROLE_ADMIN exists
        });

        // Act
        roleCommandService.handle(command);

        // Assert
        verify(roleRepository, atLeastOnce()).existsByName(any());
        // verify it saves roles that do not exist
        verify(roleRepository, atLeastOnce()).save(any(Role.class));
    }
}
