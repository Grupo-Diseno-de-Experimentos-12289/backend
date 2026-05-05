package pe.edu.upc.travelmatch.agencies.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.application.internal.commandservices.AgencyStaffCommandServiceImpl;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyStaffRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyStaffCommandServiceImpl")
public class AgencyStaffCommandServiceTests {

    @Mock
    private AgencyStaffRepository agencyStaffRepository;
    @Mock private AgencyRepository agencyRepository;

    @InjectMocks
    private AgencyStaffCommandServiceImpl agencyStaffCommandService;

    private Agency agency;
    private AgencyStaff staff;

    @BeforeEach
    void arrange_fixtures() {
        agency = new Agency(new AgencyName("Tour Perú"), "desc",
                "12345678901", "contact@tp.com", "999888777", 1L);
        staff  = new AgencyStaff(agency, "Juan", "Pérez",
                "juan@tp.com", "987654321", "Guide");
    }

    @Test
    @DisplayName("handle(CreateAgencyStaffCommand) creates and saves staff when valid")
    void handle_createStaff_allValid_staffIsSaved() {
        // Arrange
        CreateAgencyStaffCommand command = new CreateAgencyStaffCommand(
                1L, "Juan", "Pérez", "juan@tp.com", "987654321", "Guide"
        );
        when(agencyRepository.findById(1L)).thenReturn(Optional.of(agency));
        when(agencyStaffRepository.existsByEmail("juan@tp.com")).thenReturn(false);
        when(agencyStaffRepository.save(any())).thenReturn(staff);

        // Act
        var result = agencyStaffCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        verify(agencyStaffRepository).save(any(AgencyStaff.class));
    }

    @Test
    @DisplayName("handle(CreateAgencyStaffCommand) throws when agency not found")
    void handle_createStaff_agencyNotFound_throwsIllegalArgument() {
        // Arrange
        CreateAgencyStaffCommand command = new CreateAgencyStaffCommand(
                99L, "Juan", "Pérez", "juan@tp.com", "987654321", "Guide"
        );
        when(agencyRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> agencyStaffCommandService.handle(command));

        verify(agencyStaffRepository, never()).save(any());
    }

    @Test
    @DisplayName("handle(CreateAgencyStaffCommand) throws when email is already in use")
    void handle_createStaff_duplicateEmail_throwsIllegalArgument() {
        // Arrange
        CreateAgencyStaffCommand command = new CreateAgencyStaffCommand(
                1L, "Juan", "Pérez", "juan@tp.com", "987654321", "Guide"
        );
        when(agencyRepository.findById(1L)).thenReturn(Optional.of(agency));
        when(agencyStaffRepository.existsByEmail("juan@tp.com")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> agencyStaffCommandService.handle(command));
    }

    @Test
    @DisplayName("handle(UpdateAgencyStaffCommand) updates staff when found and email is free")
    void handle_updateStaff_validData_staffIsUpdated() {
        // Arrange
        UpdateAgencyStaffCommand command = new UpdateAgencyStaffCommand(
                1L, "Carlos", "López", "carlos@tp.com", "912345678", "Manager"
        );
        when(agencyStaffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(agencyStaffRepository.findByEmail("carlos@tp.com")).thenReturn(Optional.empty());
        when(agencyStaffRepository.save(any())).thenReturn(staff);

        // Act
        var result = agencyStaffCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        verify(agencyStaffRepository).save(staff);
    }

    @Test
    @DisplayName("handle(UpdateAgencyStaffCommand) returns empty when staff not found")
    void handle_updateStaff_notFound_returnsEmpty() {
        // Arrange
        UpdateAgencyStaffCommand command = new UpdateAgencyStaffCommand(
                999L, "Carlos", "López", "carlos@tp.com", "912345678", "Manager"
        );
        when(agencyStaffRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        var result = agencyStaffCommandService.handle(command);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("handle(UpdateAgencyStaffCommand) throws when email is taken by another staff")
    void handle_updateStaff_emailTakenByOther_throwsIllegalArgument() {
        // Arrange
        AgencyStaff anotherStaff = spy(new AgencyStaff(agency, "Other",
                "Person", "other@tp.com", "912345678", "Guide"));
        doReturn(2L).when(anotherStaff).getId();

        UpdateAgencyStaffCommand command = new UpdateAgencyStaffCommand(
                1L, "Carlos", "López", "other@tp.com", "912345678", "Manager"
        );
        when(agencyStaffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(agencyStaffRepository.findByEmail("other@tp.com"))
                .thenReturn(Optional.of(anotherStaff));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> agencyStaffCommandService.handle(command));
    }

    @Test
    @DisplayName("handle(DeleteAgencyStaffCommand) deletes staff when it exists")
    void handle_deleteStaff_exists_deletesSuccessfully() {
        // Arrange
        DeleteAgencyStaffCommand command = new DeleteAgencyStaffCommand(1L);
        when(agencyStaffRepository.existsById(1L)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> agencyStaffCommandService.handle(command));

        // Assert
        verify(agencyStaffRepository).deleteById(1L);
    }

    @Test
    @DisplayName("handle(DeleteAgencyStaffCommand) throws when staff not found")
    void handle_deleteStaff_notFound_throwsIllegalArgument() {
        // Arrange
        DeleteAgencyStaffCommand command = new DeleteAgencyStaffCommand(999L);
        when(agencyStaffRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> agencyStaffCommandService.handle(command));

        verify(agencyStaffRepository, never()).deleteById(any());
    }
}
