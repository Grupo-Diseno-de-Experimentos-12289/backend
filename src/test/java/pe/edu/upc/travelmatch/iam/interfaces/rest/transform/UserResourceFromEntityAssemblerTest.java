package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserResourceFromEntityAssembler}.
 *
 * This class validates the proper mapping from a User entity to a UserResource.
 */
class UserResourceFromEntityAssemblerTest {

    @Test
    @DisplayName("toResourceFromEntity should map User to UserResource correctly")
    void toResourceFromEntity_ShouldMapCorrectly() {
        // Arrange
        var user = mock(User.class);
        var role = mock(Role.class);
        
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("user@example.com");
        when(user.getFirstName()).thenReturn("John");
        when(user.getLastName()).thenReturn("Doe");
        when(user.getPhone()).thenReturn("123");
        when(user.getRoles()).thenReturn(Set.of(role));
        when(role.getStringName()).thenReturn("ROLE_TOURIST");

        // Act
        var resource = UserResourceFromEntityAssembler.toResourceFromEntity(user);

        // Assert
        assertNotNull(resource);
        assertEquals(1L, resource.id());
        assertEquals("user@example.com", resource.email());
        assertEquals("John", resource.firstName());
        assertEquals("Doe", resource.lastName());
        assertEquals("123", resource.phone());
        assertEquals(1, resource.roles().size());
        assertEquals("ROLE_TOURIST", resource.roles().get(0));
    }
}
