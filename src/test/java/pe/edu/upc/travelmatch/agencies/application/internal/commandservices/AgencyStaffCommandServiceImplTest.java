package pe.edu.upc.travelmatch.agencies.application.internal.commandservices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyStaffRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyStaffCommandServiceImpl Tests")
public class AgencyStaffCommandServiceImplTest {
    @Mock
    private AgencyStaffRepository agencyStaffRepository;
    @Mock
    private AgencyRepository agencyRepository;

    @InjectMocks
    private AgencyStaffCommandServiceImpl agencyStaffCommandService;

    private Agency agency;

    @BeforeEach
    void setUp() {
        agency = new Agency(new AgencyName("Tour Perú"), "desc", "12345678901", "info@tp.com", "999888777", 1L);
    }

    @Test
    @DisplayName("Debe crear un staff cuando no existe el email")
    void handle_createStaff_valid_returnsStaff() {
        // --- Arrange ---
        CreateAgencyStaffCommand command = new CreateAgencyStaffCommand(1L, "Juan", "Perez", "juan@tp.com", "987654321", "Guide");
        when(agencyRepository.findById(1L)).thenReturn(Optional.of(agency));
        when(agencyStaffRepository.existsByEmail("juan@tp.com")).thenReturn(false);
        when(agencyStaffRepository.save(any(AgencyStaff.class))).thenAnswer(inv -> inv.getArgument(0));

        // --- Act ---
        Optional<AgencyStaff> result = agencyStaffCommandService.handle(command);

        // --- Assert ---
        assertTrue(result.isPresent());
        verify(agencyStaffRepository).save(any(AgencyStaff.class));
    }

    @Test
    @DisplayName("Debe eliminar staff correctamente si el ID existe")
    void handle_deleteStaff_exists_deletesSuccessfully() {
        // --- Arrange ---
        DeleteAgencyStaffCommand command = new DeleteAgencyStaffCommand(1L);
        when(agencyStaffRepository.existsById(1L)).thenReturn(true);

        // --- Act ---
        assertDoesNotThrow(() -> agencyStaffCommandService.handle(command));

        // --- Assert ---
        verify(agencyStaffRepository).deleteById(1L);
    }
}
