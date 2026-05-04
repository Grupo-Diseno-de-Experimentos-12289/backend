package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.valueobjects.Roles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Implementation of the tests for RoleResourceFromEntityAssembler.
 *
 * <p>This class validates the proper mapping from a Role entity to a RoleResource.</p>
 */
class RoleResourceFromEntityAssemblerTest {

    @Test
    @DisplayName("toResourceFromEntity should map Role to RoleResource correctly")
    void toResourceFromEntity_ShouldMapRoleToRoleResource() {
        // Arrange
        var role = mock(Role.class);
        when(role.getId()).thenReturn(1L);
        when(role.getStringName()).thenReturn("ROLE_ADMIN");

        // Act
        var resource = RoleResourceFromEntityAssembler.toResourceFromEntity(role);

        // Assert
        assertNotNull(resource);
        assertEquals(1L, resource.id());
        assertEquals("ROLE_ADMIN", resource.name());
    }
}
