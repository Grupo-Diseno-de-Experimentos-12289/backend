package pe.edu.upc.travelmatch.agencies.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyDocumentByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyDocumentsByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyDocumentCommandService;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyDocumentQueryService;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyDocumentResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyDocumentResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyDocumentResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.AgencyDocumentResourceFromEntityAssembler;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.CreateAgencyDocumentCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.UpdateAgencyDocumentCommandFromResourceAssembler;

/** Documentation. */
@RestController
@RequestMapping(
    value = "/api/v1/agencies/{agencyId}/documents",
    produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agency Documents", description = "Operations related to Agency Documents")
public class AgencyDocumentsController {

  private final AgencyDocumentCommandService agencyDocumentCommandService;
  private final AgencyDocumentQueryService agencyDocumentQueryService;

  /** Constructs a new AgencyDocumentsController. */
  public AgencyDocumentsController(
      AgencyDocumentCommandService agencyDocumentCommandService,
      AgencyDocumentQueryService agencyDocumentQueryService) {
    this.agencyDocumentCommandService = agencyDocumentCommandService;
    this.agencyDocumentQueryService = agencyDocumentQueryService;
  }

  /** Create document. */
  @PostMapping
  public ResponseEntity<AgencyDocumentResource> createDocument(
      @PathVariable Long agencyId, @RequestBody @Valid CreateAgencyDocumentResource resource) {
    try {
      var command =
          CreateAgencyDocumentCommandFromResourceAssembler.toCommandFromResource(
              agencyId, resource);
      return agencyDocumentCommandService
          .handle(command)
          .map(
              document ->
                  ResponseEntity.status(HttpStatus.CREATED)
                      .body(
                          AgencyDocumentResourceFromEntityAssembler.toResourceFromEntity(document)))
          .orElseGet(() -> ResponseEntity.internalServerError().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /** Update document. */
  @PutMapping("/{documentId}")
  public ResponseEntity<AgencyDocumentResource> updateDocument(
      @PathVariable Long agencyId,
      @PathVariable Long documentId,
      @RequestBody @Valid UpdateAgencyDocumentResource resource) {
    if (!documentId.equals(resource.id())) {
      return ResponseEntity.badRequest().build();
    }
    try {
      var command =
          UpdateAgencyDocumentCommandFromResourceAssembler.toCommandFromResource(resource);
      return agencyDocumentCommandService
          .handle(command)
          .map(AgencyDocumentResourceFromEntityAssembler::toResourceFromEntity)
          .<ResponseEntity<AgencyDocumentResource>>map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /** Delete document. */
  @DeleteMapping("/{documentId}")
  public ResponseEntity<Void> deleteDocument(
      @PathVariable Long agencyId, @PathVariable Long documentId) {
    try {
      var command = new DeleteAgencyDocumentCommand(documentId);
      agencyDocumentCommandService.handle(command);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /** Get all documents by agency id. */
  @GetMapping
  public ResponseEntity<List<AgencyDocumentResource>> getAllDocumentsByAgencyId(
      @PathVariable Long agencyId) {
    var query = new GetAllAgencyDocumentsByAgencyIdQuery(agencyId);
    List<AgencyDocumentResource> documentResources =
        agencyDocumentQueryService.handle(query).stream()
            .map(AgencyDocumentResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
    return ResponseEntity.ok(documentResources);
  }

  /** Get document by id. */
  @GetMapping("/{documentId}")
  public ResponseEntity<AgencyDocumentResource> getDocumentById(
      @PathVariable Long agencyId, @PathVariable Long documentId) {
    var query = new GetAgencyDocumentByIdQuery(documentId);
    return agencyDocumentQueryService
        .handle(query)
        .map(AgencyDocumentResourceFromEntityAssembler::toResourceFromEntity)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
