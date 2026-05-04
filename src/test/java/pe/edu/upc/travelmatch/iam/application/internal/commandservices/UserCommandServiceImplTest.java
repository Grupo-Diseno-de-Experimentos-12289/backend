package pe.edu.upc.travelmatch.iam.application.internal.commandservices;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.travelmatch.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UserCommandServiceImpl}.
 *
 * <p>This class provides tests for user command handling, ensuring that user creation
 * (SignUp) and user authentication (SignIn) function correctly, verifying both
 * successful executions and error scenarios.</p>
 */
@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HashingService hashingService;

    @Mock
    private TokenService tokenService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    // ----- SignUpCommand Tests -----

    @Test
    @DisplayName("handle(SignUpCommand) should return User when creation is successful")
    void handle_SignUpCommand_ShouldReturnUser_WhenCreationIsSuccessful() {
        // Arrange
        var command = new SignUpCommand("john.doe@example.com", "password123", "John", "Doe", "123456789", new ArrayList<>());
        var role = new Role(Roles.ROLE_TOURIST);
        var expectedUser = new User(command.email(), "encodedPassword", command.firstName(), command.lastName(), command.phone(), List.of(role));

        when(userRepository.existsByEmail(command.email())).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_TOURIST)).thenReturn(Optional.of(role));
        when(hashingService.encode(command.password())).thenReturn("encodedPassword");
        when(userRepository.findByEmail(command.email())).thenReturn(Optional.of(expectedUser));

        // Act
        var result = userCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());

        verify(userRepository).existsByEmail(command.email());
        verify(roleRepository).findByName(Roles.ROLE_TOURIST);
        verify(hashingService).encode(command.password());
        verify(userRepository).save(any(User.class));
        verify(userRepository).findByEmail(command.email());
        verifyNoMoreInteractions(userRepository, roleRepository, hashingService, tokenService);
    }

    @Test
    @DisplayName("handle(SignUpCommand) should throw RuntimeException when email already exists")
    void handle_SignUpCommand_ShouldThrowRuntimeException_WhenEmailAlreadyExists() {
        // Arrange
        var command = new SignUpCommand("john.doe@example.com", "password123", "John", "Doe", "123456789", new ArrayList<>());
        when(userRepository.existsByEmail(command.email())).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userCommandService.handle(command), "Email already exists");

        verify(userRepository).existsByEmail(command.email());
        verifyNoMoreInteractions(userRepository, roleRepository, hashingService, tokenService);
    }

    @Test
    @DisplayName("handle(SignUpCommand) should throw RuntimeException when role not found")
    void handle_SignUpCommand_ShouldThrowRuntimeException_WhenRoleNotFound() {
        // Arrange
        var command = new SignUpCommand("john.doe@example.com", "password123", "John", "Doe", "123456789", List.of(new Role(Roles.ROLE_ADMIN)));

        when(userRepository.existsByEmail(command.email())).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_ADMIN)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userCommandService.handle(command), "Role not found");

        verify(userRepository).existsByEmail(command.email());
        verify(roleRepository).findByName(Roles.ROLE_ADMIN);
        verifyNoMoreInteractions(userRepository, roleRepository, hashingService, tokenService);
    }

    // ----- SignInCommand Tests -----

    @Test
    @DisplayName("handle(SignInCommand) should return User and Token when sign in is successful")
    void handle_SignInCommand_ShouldReturnUserAndToken_WhenSignInIsSuccessful() {
        // Arrange
        var command = new SignInCommand("john.doe@example.com", "password123");
        var user = new User("john.doe@example.com", "encodedPassword", "John", "Doe", "123456789", new ArrayList<>());
        var expectedToken = "jwt-token-123";

        when(userRepository.findByEmail(command.email())).thenReturn(Optional.of(user));
        when(hashingService.matches(command.password(), "encodedPassword")).thenReturn(true);
        when(tokenService.generateToken(user.getEmail())).thenReturn(expectedToken);

        // Act
        var result = userCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get().getLeft());
        assertEquals(expectedToken, result.get().getRight());

        verify(userRepository).findByEmail(command.email());
        verify(hashingService).matches(command.password(), "encodedPassword");
        verify(tokenService).generateToken(user.getEmail());
        verifyNoMoreInteractions(userRepository, roleRepository, hashingService, tokenService);
    }

    @Test
    @DisplayName("handle(SignInCommand) should throw RuntimeException when user not found")
    void handle_SignInCommand_ShouldThrowRuntimeException_WhenUserNotFound() {
        // Arrange
        var command = new SignInCommand("john.doe@example.com", "password123");
        
        when(userRepository.findByEmail(command.email())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userCommandService.handle(command), "User not found");

        verify(userRepository).findByEmail(command.email());
        verifyNoMoreInteractions(userRepository, roleRepository, hashingService, tokenService);
    }

    @Test
    @DisplayName("handle(SignInCommand) should throw RuntimeException when password is invalid")
    void handle_SignInCommand_ShouldThrowRuntimeException_WhenPasswordIsInvalid() {
        // Arrange
        var command = new SignInCommand("john.doe@example.com", "wrongpassword");
        var user = new User("john.doe@example.com", "encodedPassword", "John", "Doe", "123456789", new ArrayList<>());

        when(userRepository.findByEmail(command.email())).thenReturn(Optional.of(user));
        when(hashingService.matches(command.password(), "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userCommandService.handle(command), "Invalid password");

        verify(userRepository).findByEmail(command.email());
        verify(hashingService).matches(command.password(), "encodedPassword");
        verifyNoMoreInteractions(userRepository, roleRepository, hashingService, tokenService);
    }
}
