package pe.edu.upc.travelmatch.agencies.domain.model.valueobjects;
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



// =========================================================
//  AGENCY NAME VALUE OBJECT
// =========================================================
@Nested
@DisplayName("AgencyName Value Object")
public class AgencyNameTest {
    @Test
    @DisplayName("AgencyName stores the provided name correctly")
    void constructor_validName_nameIsStored() {
        // Arrange
        String name = "Tour Perú";

        // Act
        AgencyName agencyName = new AgencyName(name);

        // Assert
        assertEquals("Tour Perú", agencyName.getName());
    }

    @Test
    @DisplayName("AgencyName throws IllegalArgumentException when name is null")
    void constructor_nullName_throwsIllegalArgument() {
        // Arrange – null name

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new AgencyName(null));
    }

    @Test
    @DisplayName("AgencyName throws IllegalArgumentException when name is blank")
    void constructor_blankName_throwsIllegalArgument() {
        // Arrange
        String blankName = "   ";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new AgencyName(blankName));
    }

    @Test
    @DisplayName("AgencyName throws IllegalArgumentException when name is empty string")
    void constructor_emptyName_throwsIllegalArgument() {
        // Arrange
        String emptyName = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new AgencyName(emptyName));
    }

    @Test
    @DisplayName("toString() returns the stored name string")
    void toString_returnsStoredName() {
        // Arrange
        AgencyName agencyName = new AgencyName("My Agency");

        // Act
        String result = agencyName.toString();

        // Assert
        assertEquals("My Agency", result);
    }

    @Test
    @DisplayName("equals() returns true when both AgencyName objects have the same name")
    void equals_sameName_returnsTrue() {
        // Arrange
        AgencyName a = new AgencyName("Same Name");
        AgencyName b = new AgencyName("Same Name");

        // Act & Assert
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals() returns false when AgencyName objects have different names")
    void equals_differentName_returnsFalse() {
        // Arrange
        AgencyName a = new AgencyName("Agency A");
        AgencyName b = new AgencyName("Agency B");

        // Act & Assert
        assertNotEquals(a, b);
    }

}
