package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyStaffResourceFromEntityAssembler")
public class AgencyStaffResourceAssemblerTests {
  @Mock private AgencyStaff staffEntity;
  @Mock private Agency agency;

  @Test
  @DisplayName("toResourceFromEntity() maps all staff fields to AgencyStaffResource")
  void toResourceFromEntity_validStaff_allFieldsMapped() {
    // Arrange
    when(staffEntity.getId()).thenReturn(5L);
    when(staffEntity.getAgency()).thenReturn(agency);
    when(agency.getId()).thenReturn(1L);
    when(staffEntity.getFirstName()).thenReturn("Juan");
    when(staffEntity.getLastName()).thenReturn("Pérez");
    when(staffEntity.getEmail()).thenReturn("juan@tp.com");
    when(staffEntity.getPhone()).thenReturn("987654321");
    when(staffEntity.getPosition()).thenReturn("Guide");

    // Act
    var resource = AgencyStaffResourceFromEntityAssembler.toResourceFromEntity(staffEntity);

    // Assert
    assertAll(
        () -> assertEquals(5L, resource.id()),
        () -> assertEquals(1L, resource.agencyId()),
        () -> assertEquals("Juan", resource.firstName()),
        () -> assertEquals("Pérez", resource.lastName()),
        () -> assertEquals("juan@tp.com", resource.email()),
        () -> assertEquals("987654321", resource.phone()),
        () -> assertEquals("Guide", resource.position()));
  }

  @Test
  @DisplayName("toResourceFromEntity() returns non-null resource")
  void toResourceFromEntity_validStaff_returnsNonNull() {
    // Arrange
    when(staffEntity.getId()).thenReturn(1L);
    when(staffEntity.getAgency()).thenReturn(agency);
    when(agency.getId()).thenReturn(1L);
    when(staffEntity.getFirstName()).thenReturn("Ana");
    when(staffEntity.getLastName()).thenReturn("Gómez");
    when(staffEntity.getEmail()).thenReturn("ana@tp.com");
    when(staffEntity.getPhone()).thenReturn("912345678");
    when(staffEntity.getPosition()).thenReturn("Manager");

    // Act
    var resource = AgencyStaffResourceFromEntityAssembler.toResourceFromEntity(staffEntity);

    // Assert
    assertNotNull(resource);
  }
}
