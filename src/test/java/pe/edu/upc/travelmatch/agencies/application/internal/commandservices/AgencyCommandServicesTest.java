package pe.edu.upc.travelmatch.agencies.application.internal.commandservices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.*;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyDocumentRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyStaffRepository;
import pe.edu.upc.travelmatch.agencies.interfaces.acl.AgenciesContextFacade;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
public class AgencyCommandServicesTest {
    // =========================================================
    //  AGENCY COMMAND SERVICE
    // =========================================================
    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("AgencyCommandServiceImpl")
    class AgencyCommandServiceTests {

        @Mock private AgencyRepository     agencyRepository;
        @Mock private AgenciesContextFacade agenciesContextFacade;

        @InjectMocks
        private AgencyCommandServiceImpl agencyCommandService;

        private Agency existingAgency;

        @BeforeEach
        void arrange_existingAgency() {
            existingAgency = new Agency(
                    new AgencyName("Tour Perú"), "Wonderful tours",
                    "12345678901", "info@tourperu.com", "999888777", 1L
            );
        }

        // ── Create ───────────────────────────────────────────

        @Test
        @DisplayName("handle(CreateAgencyCommand) saves agency when all validations pass")
        void handle_createAgency_allValid_agencyIsSaved() {
            // Arrange
            CreateAgencyCommand command = new CreateAgencyCommand(
                    "Tour Perú", "Great tours", "12345678901",
                    "info@tourperu.com", "999888777", 1L
            );
            when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
            when(agencyRepository.existsByUserId(1L)).thenReturn(false);
            when(agencyRepository.existsByRuc("12345678901")).thenReturn(false);
            when(agencyRepository.existsByContactEmail("info@tourperu.com")).thenReturn(false);
            when(agencyRepository.save(any(Agency.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            assertDoesNotThrow(() -> agencyCommandService.handle(command));

            // Assert
            verify(agencyRepository, times(1)).save(any(Agency.class));
        }

        @Test
        @DisplayName("handle(CreateAgencyCommand) saves agency with correct field values")
        void handle_createAgency_savedWithCorrectFields() {
            // Arrange
            CreateAgencyCommand command = new CreateAgencyCommand(
                    "Tour Perú", "Great tours", "12345678901",
                    "info@tourperu.com", "999888777", 1L
            );
            when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
            when(agencyRepository.existsByUserId(1L)).thenReturn(false);
            when(agencyRepository.existsByRuc("12345678901")).thenReturn(false);
            when(agencyRepository.existsByContactEmail("info@tourperu.com")).thenReturn(false);

            ArgumentCaptor<Agency> captor = ArgumentCaptor.forClass(Agency.class);
            when(agencyRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

            // Act
            agencyCommandService.handle(command);

            // Assert
            Agency saved = captor.getValue();
            assertAll(
                    () -> assertEquals("Tour Perú", saved.getName().getName()),
                    () -> assertEquals("12345678901", saved.getRuc()),
                    () -> assertEquals("info@tourperu.com", saved.getContactEmail()),
                    () -> assertEquals(1L, saved.getUserId())
            );
        }

        @Test
        @DisplayName("handle(CreateAgencyCommand) throws when user not found in IAM")
        void handle_createAgency_userNotFound_throwsIllegalArgument() {
            // Arrange
            CreateAgencyCommand command = new CreateAgencyCommand(
                    "Tour Perú", "desc", "12345678901",
                    "info@tourperu.com", "999888777", 99L
            );
            when(agenciesContextFacade.existsUserById(99L)).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> agencyCommandService.handle(command));

            verify(agencyRepository, never()).save(any());
        }

        @Test
        @DisplayName("handle(CreateAgencyCommand) throws when agency already exists for userId")
        void handle_createAgency_duplicateUserId_throwsIllegalArgument() {
            // Arrange
            CreateAgencyCommand command = new CreateAgencyCommand(
                    "Tour Perú", "desc", "12345678901",
                    "info@tourperu.com", "999888777", 1L
            );
            when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
            when(agencyRepository.existsByUserId(1L)).thenReturn(true);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> agencyCommandService.handle(command));
        }

        @Test
        @DisplayName("handle(CreateAgencyCommand) throws when RUC is already taken")
        void handle_createAgency_duplicateRuc_throwsIllegalArgument() {
            // Arrange
            CreateAgencyCommand command = new CreateAgencyCommand(
                    "Tour Perú", "desc", "12345678901",
                    "info@tourperu.com", "999888777", 1L
            );
            when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
            when(agencyRepository.existsByUserId(1L)).thenReturn(false);
            when(agencyRepository.existsByRuc("12345678901")).thenReturn(true);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> agencyCommandService.handle(command));
        }

        @Test
        @DisplayName("handle(CreateAgencyCommand) throws when contact email is already registered")
        void handle_createAgency_duplicateEmail_throwsIllegalArgument() {
            // Arrange
            CreateAgencyCommand command = new CreateAgencyCommand(
                    "Tour Perú", "desc", "12345678901",
                    "info@tourperu.com", "999888777", 1L
            );
            when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
            when(agencyRepository.existsByUserId(1L)).thenReturn(false);
            when(agencyRepository.existsByRuc("12345678901")).thenReturn(false);
            when(agencyRepository.existsByContactEmail("info@tourperu.com")).thenReturn(true);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> agencyCommandService.handle(command));
        }

        // ── Update ───────────────────────────────────────────

        @Test
        @DisplayName("handle(UpdateAgencyCommand) updates and returns agency when validations pass")
        void handle_updateAgency_validData_returnsUpdatedAgency() {
            // Arrange
            UpdateAgencyCommand command = new UpdateAgencyCommand(
                    1L, "New Name", "New desc", "new@email.com", "111222333"
            );
            when(agencyRepository.findById(1L)).thenReturn(Optional.of(existingAgency));
            when(agencyRepository.existsByContactEmailAndIdIsNot("new@email.com", 1L))
                    .thenReturn(false);
            when(agencyRepository.save(existingAgency)).thenReturn(existingAgency);

            // Act
            Agency result = agencyCommandService.handle(command);

            // Assert
            assertNotNull(result);
            verify(agencyRepository).save(existingAgency);
        }

        @Test
        @DisplayName("handle(UpdateAgencyCommand) throws when agency does not exist")
        void handle_updateAgency_notFound_throwsIllegalArgument() {
            // Arrange
            UpdateAgencyCommand command = new UpdateAgencyCommand(
                    999L, "Name", "desc", "e@mail.com", "123456789"
            );
            when(agencyRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> agencyCommandService.handle(command));
        }

        @Test
        @DisplayName("handle(UpdateAgencyCommand) throws when email is taken by another agency")
        void handle_updateAgency_emailTakenByOther_throwsIllegalArgument() {
            // Arrange
            UpdateAgencyCommand command = new UpdateAgencyCommand(
                    1L, null, null, "taken@email.com", null
            );
            when(agencyRepository.findById(1L)).thenReturn(Optional.of(existingAgency));
            when(agencyRepository.existsByContactEmailAndIdIsNot("taken@email.com", 1L))
                    .thenReturn(true);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> agencyCommandService.handle(command));
        }

        // ── Delete ───────────────────────────────────────────

        @Test
        @DisplayName("handle(DeleteAgencyCommand) deletes agency when it exists")
        void handle_deleteAgency_exists_deletesSuccessfully() {
            // Arrange
            DeleteAgencyCommand command = new DeleteAgencyCommand(1L);
            when(agencyRepository.existsById(1L)).thenReturn(true);

            // Act
            assertDoesNotThrow(() -> agencyCommandService.handle(command));

            // Assert
            verify(agencyRepository).deleteById(1L);
        }

        @Test
        @DisplayName("handle(DeleteAgencyCommand) throws when agency does not exist")
        void handle_deleteAgency_notFound_throwsIllegalArgument() {
            // Arrange
            DeleteAgencyCommand command = new DeleteAgencyCommand(999L);
            when(agencyRepository.existsById(999L)).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> agencyCommandService.handle(command));

            verify(agencyRepository, never()).deleteById(any());
        }
    }

    // =========================================================
    //  AGENCY STAFF COMMAND SERVICE
    // =========================================================
    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("AgencyStaffCommandServiceImpl")
    class AgencyStaffCommandServiceTests {

        @Mock private AgencyStaffRepository agencyStaffRepository;
        @Mock private AgencyRepository      agencyRepository;

        @InjectMocks
        private AgencyStaffCommandServiceImpl agencyStaffCommandService;

        private Agency     agency;
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

    // =========================================================
    //  AGENCY DOCUMENT COMMAND SERVICE
    // =========================================================
    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("AgencyDocumentCommandServiceImpl")
    class AgencyDocumentCommandServiceTests {

        @Mock private AgencyDocumentRepository agencyDocumentRepository;
        @Mock private AgencyRepository         agencyRepository;

        @InjectMocks
        private AgencyDocumentCommandServiceImpl agencyDocumentCommandService;

        private Agency        agency;
        private AgencyDocument document;

        @BeforeEach
        void arrange_fixtures() {
            agency   = new Agency(new AgencyName("Tour Perú"), "desc",
                    "12345678901", "contact@tp.com", "999888777", 1L);
            document = new AgencyDocument(agency, "RUC",
                    "http://example.com/ruc.pdf", "Tax ID document");
        }

        @Test
        @DisplayName("handle(CreateAgencyDocumentCommand) creates document when agency exists")
        void handle_createDocument_agencyExists_documentIsSaved() {
            // Arrange
            CreateAgencyDocumentCommand command = new CreateAgencyDocumentCommand(
                    1L, "RUC", "http://example.com/ruc.pdf", "Tax ID"
            );
            when(agencyRepository.findById(1L)).thenReturn(Optional.of(agency));
            when(agencyDocumentRepository.save(any())).thenReturn(document);

            // Act
            var result = agencyDocumentCommandService.handle(command);

            // Assert
            assertTrue(result.isPresent());
            verify(agencyDocumentRepository).save(any(AgencyDocument.class));
        }

        @Test
        @DisplayName("handle(CreateAgencyDocumentCommand) throws when agency not found")
        void handle_createDocument_agencyNotFound_throwsIllegalArgument() {
            // Arrange
            CreateAgencyDocumentCommand command = new CreateAgencyDocumentCommand(
                    99L, "RUC", "http://example.com/ruc.pdf", "Tax ID"
            );
            when(agencyRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> agencyDocumentCommandService.handle(command));

            verify(agencyDocumentRepository, never()).save(any());
        }

        @Test
        @DisplayName("handle(UpdateAgencyDocumentCommand) updates fields and returns document")
        void handle_updateDocument_exists_updatedFieldsPersisted() {
            // Arrange
            UpdateAgencyDocumentCommand command = new UpdateAgencyDocumentCommand(
                    1L, "LICENCIA", "http://new.com/lic.pdf", "Operating license"
            );
            when(agencyDocumentRepository.findById(1L)).thenReturn(Optional.of(document));
            when(agencyDocumentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            // Act
            var result = agencyDocumentCommandService.handle(command);

            // Assert
            assertTrue(result.isPresent());
            assertAll(
                    () -> assertEquals("LICENCIA",                   result.get().getDocumentType()),
                    () -> assertEquals("http://new.com/lic.pdf",     result.get().getDocumentUrl()),
                    () -> assertEquals("Operating license",          result.get().getDescription())
            );
        }

        @Test
        @DisplayName("handle(UpdateAgencyDocumentCommand) returns empty when document not found")
        void handle_updateDocument_notFound_returnsEmpty() {
            // Arrange
            UpdateAgencyDocumentCommand command = new UpdateAgencyDocumentCommand(
                    999L, "LICENCIA", "http://new.com/lic.pdf", "desc"
            );
            when(agencyDocumentRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            var result = agencyDocumentCommandService.handle(command);

            // Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("handle(DeleteAgencyDocumentCommand) deletes document when it exists")
        void handle_deleteDocument_exists_deletesSuccessfully() {
            // Arrange
            DeleteAgencyDocumentCommand command = new DeleteAgencyDocumentCommand(1L);
            when(agencyDocumentRepository.existsById(1L)).thenReturn(true);

            // Act
            assertDoesNotThrow(() -> agencyDocumentCommandService.handle(command));

            // Assert
            verify(agencyDocumentRepository).deleteById(1L);
        }

        @Test
        @DisplayName("handle(DeleteAgencyDocumentCommand) throws when document not found")
        void handle_deleteDocument_notFound_throwsIllegalArgument() {
            // Arrange
            DeleteAgencyDocumentCommand command = new DeleteAgencyDocumentCommand(999L);
            when(agencyDocumentRepository.existsById(999L)).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> agencyDocumentCommandService.handle(command));

            verify(agencyDocumentRepository, never()).deleteById(any());
        }
    }
}
