package pe.edu.upc.travelmatch.agencies.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyStaffByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyStaffByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyStaffCommandService;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyStaffQueryService;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyStaffResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyStaffResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyStaffResource;

@ExtendWith(MockitoExtension.class)
class AgencyStaffControllerTest {

  @Mock private AgencyStaffCommandService commandService;
  @Mock private AgencyStaffQueryService queryService;
  @InjectMocks private AgencyStaffController controller;

  private AgencyStaff staff;

  @BeforeEach
  void setUp() {
    Agency agency =
        new Agency(
            new AgencyName("Tour Peru"),
            "Description",
            "12345678901",
            "tour@peru.com",
            "987654321",
            1L);
    staff = new AgencyStaff(agency, "John", "Doe", "john@peru.com", "987654321", "Manager");
  }

  @Test
  void createStaff_returnsCreated_whenSuccess() {
    CreateAgencyStaffResource resource =
        new CreateAgencyStaffResource("John", "Doe", "john@peru.com", "987654321", "Manager");
    when(commandService.handle(any(CreateAgencyStaffCommand.class))).thenReturn(Optional.of(staff));

    ResponseEntity<AgencyStaffResource> response = controller.createStaff(1L, resource);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void createStaff_returnsInternalServerError_whenServiceReturnsEmpty() {
    CreateAgencyStaffResource resource =
        new CreateAgencyStaffResource("John", "Doe", "john@peru.com", "987654321", "Manager");
    when(commandService.handle(any(CreateAgencyStaffCommand.class))).thenReturn(Optional.empty());

    ResponseEntity<AgencyStaffResource> response = controller.createStaff(1L, resource);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void createStaff_returnsBadRequest_whenIllegalArgumentThrown() {
    CreateAgencyStaffResource resource =
        new CreateAgencyStaffResource("John", "Doe", "john@peru.com", "987654321", "Manager");
    when(commandService.handle(any(CreateAgencyStaffCommand.class)))
        .thenThrow(new IllegalArgumentException("Invalid"));

    ResponseEntity<AgencyStaffResource> response = controller.createStaff(1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void updateStaff_returnsBadRequest_whenIdMismatch() {
    UpdateAgencyStaffResource resource =
        new UpdateAgencyStaffResource(
            2L, "John", "Doe", "john@peru.com", "987654321", "Manager");

    ResponseEntity<AgencyStaffResource> response = controller.updateStaff(1L, 1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void updateStaff_returnsOk_whenSuccess() {
    UpdateAgencyStaffResource resource =
        new UpdateAgencyStaffResource(
            1L, "John", "Doe", "john@peru.com", "987654321", "Director");
    when(commandService.handle(any(UpdateAgencyStaffCommand.class))).thenReturn(Optional.of(staff));

    ResponseEntity<AgencyStaffResource> response = controller.updateStaff(1L, 1L, resource);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void updateStaff_returnsNotFound_whenServiceReturnsEmpty() {
    UpdateAgencyStaffResource resource =
        new UpdateAgencyStaffResource(
            1L, "John", "Doe", "john@peru.com", "987654321", "Director");
    when(commandService.handle(any(UpdateAgencyStaffCommand.class))).thenReturn(Optional.empty());

    ResponseEntity<AgencyStaffResource> response = controller.updateStaff(1L, 1L, resource);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void updateStaff_returnsBadRequest_whenIllegalArgumentThrown() {
    UpdateAgencyStaffResource resource =
        new UpdateAgencyStaffResource(
            1L, "John", "Doe", "john@peru.com", "987654321", "Director");
    when(commandService.handle(any(UpdateAgencyStaffCommand.class)))
        .thenThrow(new IllegalArgumentException("Invalid"));

    ResponseEntity<AgencyStaffResource> response = controller.updateStaff(1L, 1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void deleteStaff_returnsNoContent_whenSuccess() {
    ResponseEntity<Void> response = controller.deleteStaff(1L, 1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(commandService).handle(any(DeleteAgencyStaffCommand.class));
  }

  @Test
  void deleteStaff_returnsNotFound_whenIllegalArgumentThrown() {
    doThrow(new IllegalArgumentException("Not found"))
        .when(commandService)
        .handle(any(DeleteAgencyStaffCommand.class));

    ResponseEntity<Void> response = controller.deleteStaff(1L, 1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void getAllStaffByAgencyId_returnsOkWithList() {
    when(queryService.handle(any(GetAllAgencyStaffByAgencyIdQuery.class)))
        .thenReturn(List.of(staff));

    ResponseEntity<List<AgencyStaffResource>> response = controller.getAllStaffByAgencyId(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getAllStaffByAgencyId_returnsOkWithEmptyList() {
    when(queryService.handle(any(GetAllAgencyStaffByAgencyIdQuery.class))).thenReturn(List.of());

    ResponseEntity<List<AgencyStaffResource>> response = controller.getAllStaffByAgencyId(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
  }

  @Test
  void getStaffById_returnsOk_whenFound() {
    when(queryService.handle(any(GetAgencyStaffByIdQuery.class))).thenReturn(Optional.of(staff));

    ResponseEntity<AgencyStaffResource> response = controller.getStaffById(1L, 1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void getStaffById_returnsNotFound_whenMissing() {
    when(queryService.handle(any(GetAgencyStaffByIdQuery.class))).thenReturn(Optional.empty());

    ResponseEntity<AgencyStaffResource> response = controller.getStaffById(1L, 99L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
