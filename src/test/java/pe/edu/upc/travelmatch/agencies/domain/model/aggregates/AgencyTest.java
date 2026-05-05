package pe.edu.upc.travelmatch.agencies.domain.model.aggregates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.application.internal.queryservices.AgencyDocumentQueryServiceImpl;
import pe.edu.upc.travelmatch.agencies.application.internal.queryservices.AgencyQueryServiceImpl;
import pe.edu.upc.travelmatch.agencies.application.internal.queryservices.AgencyStaffQueryServiceImpl;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.*;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyDocumentRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyStaffRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AgencyTest {
    // =========================================================
    //  AGENCY AGGREGATE DOMAIN BEHAVIOR
    // =========================================================
    @Nested
    @DisplayName("Agency Aggregate - Domain Behavior")
    class AgencyAggregateTests {

        private Agency agency;

        @BeforeEach
        void arrange_agency() {
            agency = new Agency(
                    new AgencyName("Tour Perú"),
                    "Wonderful tours in Peru",
                    "12345678901",
                    "info@tourperu.com",
                    "999888777",
                    10L
            );
        }

        @Test
        @DisplayName("Agency stores all construction fields correctly")
        void constructor_allFields_storedCorrectly() {
            // Arrange – done in @BeforeEach

            // Act – just read values

            // Assert
            assertAll(
                    () -> assertEquals("Tour Perú",           agency.getName().getName()),
                    () -> assertEquals("Wonderful tours in Peru", agency.getDescription()),
                    () -> assertEquals("12345678901",          agency.getRuc()),
                    () -> assertEquals("info@tourperu.com",   agency.getContactEmail()),
                    () -> assertEquals("999888777",           agency.getContactPhone()),
                    () -> assertEquals(10L,                   agency.getUserId())
            );
        }

        @Test
        @DisplayName("updateDetails() replaces name when new AgencyName is provided")
        void updateDetails_newName_nameIsUpdated() {
            // Arrange
            AgencyName newName = new AgencyName("New Agency Name");

            // Act
            agency.updateDetails(newName, null, null, null);

            // Assert
            assertEquals("New Agency Name", agency.getName().getName());
        }

        @Test
        @DisplayName("updateDetails() replaces description when value is provided")
        void updateDetails_newDescription_descriptionIsUpdated() {
            // Arrange
            String newDescription = "Updated description";

            // Act
            agency.updateDetails(null, newDescription, null, null);

            // Assert
            assertEquals("Updated description", agency.getDescription());
        }

        @Test
        @DisplayName("updateDetails() replaces contact email when value is provided")
        void updateDetails_newEmail_emailIsUpdated() {
            // Arrange
            String newEmail = "new@tourperu.com";

            // Act
            agency.updateDetails(null, null, newEmail, null);

            // Assert
            assertEquals("new@tourperu.com", agency.getContactEmail());
        }

        @Test
        @DisplayName("updateDetails() replaces contact phone when value is provided")
        void updateDetails_newPhone_phoneIsUpdated() {
            // Arrange
            String newPhone = "111000999";

            // Act
            agency.updateDetails(null, null, null, newPhone);

            // Assert
            assertEquals("111000999", agency.getContactPhone());
        }

        @Test
        @DisplayName("updateDetails() does not change any field when all nulls are passed")
        void updateDetails_allNulls_noFieldChanges() {
            // Arrange
            String originalEmail = agency.getContactEmail();

            // Act
            agency.updateDetails(null, null, null, null);

            // Assert
            assertEquals(originalEmail, agency.getContactEmail());
        }
    }
}
