package pe.edu.upc.travelmatch.agencies.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgenciesQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyCommandService;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyQueryService;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyResource;

@ExtendWith(MockitoExtension.class)
class AgencyControllerTest {

  @Mock private AgencyCommandService agencyCommandService;
  @Mock private AgencyQueryService agencyQueryService;
  @InjectMocks private AgencyController agencyController;

  private Agency agency;

  @BeforeEach
  void setUp() {
    agency =
        new Agency(
            new AgencyName("Tour Peru"),
            "Description",
            "12345678901",
            "tour@peru.com",
            "987654321",
            1L);
  }

  @Test
  void createAgency_returnsCreated_whenSuccess() {
    CreateAgencyResource resource =
        new CreateAgencyResource(
            "Tour Peru", "Description", "12345678901", "tour@peru.com", "987654321", 1L);
    when(agencyCommandService.handle(any(CreateAgencyCommand.class))).thenReturn(1L);
    when(agencyQueryService.handle(any(GetAgencyByIdQuery.class))).thenReturn(Optional.of(agency));

    ResponseEntity<AgencyResource> response = agencyController.createAgency(resource);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Tour Peru", response.getBody().name());
  }

  @Test
  void createAgency_returnsInternalServerError_whenQueryReturnsEmpty() {
    CreateAgencyResource resource =
        new CreateAgencyResource(
            "Tour Peru", "Description", "12345678901", "tour@peru.com", "987654321", 1L);
    when(agencyCommandService.handle(any(CreateAgencyCommand.class))).thenReturn(1L);
    when(agencyQueryService.handle(any(GetAgencyByIdQuery.class))).thenReturn(Optional.empty());

    ResponseEntity<AgencyResource> response = agencyController.createAgency(resource);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void createAgency_returnsBadRequest_whenIllegalArgumentThrown() {
    CreateAgencyResource resource =
        new CreateAgencyResource(
            "Tour Peru", "Description", "12345678901", "tour@peru.com", "987654321", 1L);
    when(agencyCommandService.handle(any(CreateAgencyCommand.class)))
        .thenThrow(new IllegalArgumentException("RUC exists"));

    ResponseEntity<AgencyResource> response = agencyController.createAgency(resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void getAgencyById_returnsOk_whenFound() {
    when(agencyQueryService.handle(any(GetAgencyByIdQuery.class))).thenReturn(Optional.of(agency));

    ResponseEntity<AgencyResource> response = agencyController.getAgencyById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Tour Peru", response.getBody().name());
  }

  @Test
  void getAgencyById_returnsNotFound_whenMissing() {
    when(agencyQueryService.handle(any(GetAgencyByIdQuery.class))).thenReturn(Optional.empty());

    ResponseEntity<AgencyResource> response = agencyController.getAgencyById(99L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void getAllAgencies_returnsOkWithList() {
    when(agencyQueryService.handle(any(GetAllAgenciesQuery.class))).thenReturn(List.of(agency));

    ResponseEntity<List<AgencyResource>> response = agencyController.getAllAgencies();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void updateAgency_returnsOk_whenSuccess() {
    UpdateAgencyResource resource =
        new UpdateAgencyResource("New Name", "New Desc", null, "new@peru.com", "999999999");
    when(agencyCommandService.handle(any(UpdateAgencyCommand.class))).thenReturn(agency);

    ResponseEntity<AgencyResource> response = agencyController.updateAgency(1L, resource);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void updateAgency_returnsBadRequest_whenIllegalArgumentThrown() {
    UpdateAgencyResource resource =
        new UpdateAgencyResource("New Name", "New Desc", null, "new@peru.com", "999999999");
    when(agencyCommandService.handle(any(UpdateAgencyCommand.class)))
        .thenThrow(new IllegalArgumentException("Not found"));

    ResponseEntity<AgencyResource> response = agencyController.updateAgency(1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void deleteAgency_returnsNoContent_whenSuccess() {
    ResponseEntity<Void> response = agencyController.deleteAgency(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
    verify(agencyCommandService).handle(any(DeleteAgencyCommand.class));
  }

  @Test
  void deleteAgency_returnsNotFound_whenIllegalArgumentThrown() {
    doThrow(new IllegalArgumentException("Not found"))
        .when(agencyCommandService)
        .handle(any(DeleteAgencyCommand.class));

    ResponseEntity<Void> response = agencyController.deleteAgency(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
