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
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyDocumentByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyDocumentsByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyDocumentCommandService;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyDocumentQueryService;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyDocumentResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyDocumentResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyDocumentResource;

@ExtendWith(MockitoExtension.class)
class AgencyDocumentsControllerTest {

  @Mock private AgencyDocumentCommandService commandService;
  @Mock private AgencyDocumentQueryService queryService;
  @InjectMocks private AgencyDocumentsController controller;

  private AgencyDocument document;

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
    document = new AgencyDocument(agency, "RUC", "http://example.com/ruc.pdf", "RUC document");
  }

  @Test
  void createDocument_returnsCreated_whenSuccess() {
    CreateAgencyDocumentResource resource =
        new CreateAgencyDocumentResource("RUC", "http://example.com/ruc.pdf", "RUC document");
    when(commandService.handle(any(CreateAgencyDocumentCommand.class)))
        .thenReturn(Optional.of(document));

    ResponseEntity<AgencyDocumentResource> response = controller.createDocument(1L, resource);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void createDocument_returnsInternalServerError_whenServiceReturnsEmpty() {
    CreateAgencyDocumentResource resource =
        new CreateAgencyDocumentResource("RUC", "http://example.com/ruc.pdf", "RUC document");
    when(commandService.handle(any(CreateAgencyDocumentCommand.class)))
        .thenReturn(Optional.empty());

    ResponseEntity<AgencyDocumentResource> response = controller.createDocument(1L, resource);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void createDocument_returnsBadRequest_whenIllegalArgumentThrown() {
    CreateAgencyDocumentResource resource =
        new CreateAgencyDocumentResource("RUC", "http://example.com/ruc.pdf", "RUC document");
    when(commandService.handle(any(CreateAgencyDocumentCommand.class)))
        .thenThrow(new IllegalArgumentException("Invalid"));

    ResponseEntity<AgencyDocumentResource> response = controller.createDocument(1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void updateDocument_returnsBadRequest_whenIdMismatch() {
    UpdateAgencyDocumentResource resource =
        new UpdateAgencyDocumentResource(2L, "RUC", "http://example.com/ruc.pdf", "Desc");

    ResponseEntity<AgencyDocumentResource> response = controller.updateDocument(1L, 1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void updateDocument_returnsOk_whenSuccess() {
    UpdateAgencyDocumentResource resource =
        new UpdateAgencyDocumentResource(1L, "RUC", "http://example.com/ruc.pdf", "Updated desc");
    when(commandService.handle(any(UpdateAgencyDocumentCommand.class)))
        .thenReturn(Optional.of(document));

    ResponseEntity<AgencyDocumentResource> response = controller.updateDocument(1L, 1L, resource);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void updateDocument_returnsNotFound_whenServiceReturnsEmpty() {
    UpdateAgencyDocumentResource resource =
        new UpdateAgencyDocumentResource(1L, "RUC", "http://example.com/ruc.pdf", "Updated desc");
    when(commandService.handle(any(UpdateAgencyDocumentCommand.class)))
        .thenReturn(Optional.empty());

    ResponseEntity<AgencyDocumentResource> response = controller.updateDocument(1L, 1L, resource);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void updateDocument_returnsBadRequest_whenIllegalArgumentThrown() {
    UpdateAgencyDocumentResource resource =
        new UpdateAgencyDocumentResource(1L, "RUC", "http://example.com/ruc.pdf", "Updated desc");
    when(commandService.handle(any(UpdateAgencyDocumentCommand.class)))
        .thenThrow(new IllegalArgumentException("Invalid"));

    ResponseEntity<AgencyDocumentResource> response = controller.updateDocument(1L, 1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void deleteDocument_returnsNoContent_whenSuccess() {
    ResponseEntity<Void> response = controller.deleteDocument(1L, 1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(commandService).handle(any(DeleteAgencyDocumentCommand.class));
  }

  @Test
  void deleteDocument_returnsNotFound_whenIllegalArgumentThrown() {
    doThrow(new IllegalArgumentException("Not found"))
        .when(commandService)
        .handle(any(DeleteAgencyDocumentCommand.class));

    ResponseEntity<Void> response = controller.deleteDocument(1L, 1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void getAllDocumentsByAgencyId_returnsOkWithList() {
    when(queryService.handle(any(GetAllAgencyDocumentsByAgencyIdQuery.class)))
        .thenReturn(List.of(document));

    ResponseEntity<List<AgencyDocumentResource>> response =
        controller.getAllDocumentsByAgencyId(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getAllDocumentsByAgencyId_returnsOkWithEmptyList() {
    when(queryService.handle(any(GetAllAgencyDocumentsByAgencyIdQuery.class)))
        .thenReturn(List.of());

    ResponseEntity<List<AgencyDocumentResource>> response =
        controller.getAllDocumentsByAgencyId(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
  }

  @Test
  void getDocumentById_returnsOk_whenFound() {
    when(queryService.handle(any(GetAgencyDocumentByIdQuery.class)))
        .thenReturn(Optional.of(document));

    ResponseEntity<AgencyDocumentResource> response = controller.getDocumentById(1L, 1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void getDocumentById_returnsNotFound_whenMissing() {
    when(queryService.handle(any(GetAgencyDocumentByIdQuery.class))).thenReturn(Optional.empty());

    ResponseEntity<AgencyDocumentResource> response = controller.getDocumentById(1L, 99L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
