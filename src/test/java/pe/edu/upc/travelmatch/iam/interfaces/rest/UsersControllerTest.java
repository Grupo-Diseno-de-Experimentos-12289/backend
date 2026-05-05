package pe.edu.upc.travelmatch.iam.interfaces.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.travelmatch.iam.domain.services.UserQueryService;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.UserResource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Implementation of the tests for UsersController REST endpoints.
 *
 * <p>This controller provides endpoints to retrieve user information by delegating
 * queries to the UserQueryService.</p>
 */
@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UserQueryService userQueryService;

    @InjectMocks
    private UsersController usersController;

    @Test
    @DisplayName("getAllUsers should return 200 OK and list of UserResource")
    void getAllUsers_ShouldReturnOkAndListOfUserResource() {
        // Arrange
        var userSpy = spy(new User("john.doe@example.com", "encoded", "John", "Doe", "123"));
        when(userSpy.getId()).thenReturn(1L);
        when(userSpy.getRoles()).thenReturn(Set.of());

        when(userQueryService.handle(any(GetAllUsersQuery.class))).thenReturn(List.of(userSpy));

        // Act
        ResponseEntity<List<UserResource>> response = usersController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        
        verify(userQueryService).handle(any(GetAllUsersQuery.class));
        verifyNoMoreInteractions(userQueryService);
    }

    @Test
    @DisplayName("getUserById should return 200 OK and UserResource when found")
    void getUserById_ShouldReturnOkAndUserResource_WhenFound() {
        // Arrange
        var expectedId = 1L;
        var userSpy = spy(new User("john.doe@example.com", "encoded", "John", "Doe", "123"));
        when(userSpy.getId()).thenReturn(expectedId);
        when(userSpy.getRoles()).thenReturn(Set.of());

        when(userQueryService.handle(any(GetUserByIdQuery.class))).thenReturn(Optional.of(userSpy));

        // Act
        ResponseEntity<UserResource> response = usersController.getUserById(expectedId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().firstName());
        
        verify(userQueryService).handle(any(GetUserByIdQuery.class));
        verifyNoMoreInteractions(userQueryService);
    }

    @Test
    @DisplayName("getUserById should return 404 Not Found when not found")
    void getUserById_ShouldReturnNotFound_WhenNotFound() {
        // Arrange
        var expectedId = 1L;

        when(userQueryService.handle(any(GetUserByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserResource> response = usersController.getUserById(expectedId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(userQueryService).handle(any(GetUserByIdQuery.class));
        verifyNoMoreInteractions(userQueryService);
    }
}
