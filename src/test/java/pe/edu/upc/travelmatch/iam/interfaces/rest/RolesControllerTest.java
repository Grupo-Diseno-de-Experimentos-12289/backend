package pe.edu.upc.travelmatch.iam.interfaces.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllRolesQuery;
import pe.edu.upc.travelmatch.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.travelmatch.iam.domain.services.RoleQueryService;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.RoleResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RolesController}.
 *
 * This class validates the roles endpoint, ensuring it returns all available roles
 * correctly mapped to resources.
 */
@ExtendWith(MockitoExtension.class)
class RolesControllerTest {

    @Mock private RoleQueryService roleQueryService;
    @InjectMocks private RolesController rolesController;

    @Test
    @DisplayName("getAllRoles should return 200 OK and list of RoleResource")
    void getAllRoles_ShouldReturnOkAndListOfRoleResource() {
        // Arrange
        var roleSpy = spy(new Role(Roles.ROLE_ADMIN));
        when(roleSpy.getId()).thenReturn(1L);

        when(roleQueryService.handle(any(GetAllRolesQuery.class))).thenReturn(List.of(roleSpy));

        // Act
        ResponseEntity<List<RoleResource>> response = rolesController.getAllRoles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        
        verify(roleQueryService).handle(any(GetAllRolesQuery.class));
        verifyNoMoreInteractions(roleQueryService);
    }
}
