package pe.edu.upc.travelmatch.agencies.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyDocumentsByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyDocumentByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyDocumentCommandService;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyDocumentQueryService;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyDocumentResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyDocumentResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyDocumentResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.AgencyDocumentResourceFromEntityAssembler;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.CreateAgencyDocumentCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.UpdateAgencyDocumentCommandFromResourceAssembler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/agencies/{agencyId}/documents", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agency Documents", description = "Operations related to Agency Documents")
public class AgencyDocumentsController {

    private final AgencyDocumentCommandService agencyDocumentCommandService;
    private final AgencyDocumentQueryService agencyDocumentQueryService;

    public AgencyDocumentsController(AgencyDocumentCommandService agencyDocumentCommandService, AgencyDocumentQueryService agencyDocumentQueryService) {
        this.agencyDocumentCommandService = agencyDocumentCommandService;
        this.agencyDocumentQueryService = agencyDocumentQueryService;
    }

    @PostMapping
    public ResponseEntity<AgencyDocumentResource> createDocument(
            @PathVariable Long agencyId,
            @RequestBody @Valid CreateAgencyDocumentResource resource) {
        try {
            var command = CreateAgencyDocumentCommandFromResourceAssembler.toCommandFromResource(agencyId, resource);
            return agencyDocumentCommandService.handle(command)
                    .map(document -> new ResponseEntity<>(AgencyDocumentResourceFromEntityAssembler.toResourceFromEntity(document), HttpStatus.CREATED))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<AgencyDocumentResource> updateDocument(
            @PathVariable Long agencyId,
            @PathVariable Long documentId,
            @RequestBody @Valid UpdateAgencyDocumentResource resource) {
        if (!documentId.equals(resource.id())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            var command = UpdateAgencyDocumentCommandFromResourceAssembler.toCommandFromResource(resource);
            return agencyDocumentCommandService.handle(command)
                    .map(document -> new ResponseEntity<>(AgencyDocumentResourceFromEntityAssembler.toResourceFromEntity(document), HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long agencyId, @PathVariable Long documentId) {
        try {
            var command = new DeleteAgencyDocumentCommand(documentId);
            agencyDocumentCommandService.handle(command);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AgencyDocumentResource>> getAllDocumentsByAgencyId(@PathVariable Long agencyId) {
        var query = new GetAllAgencyDocumentsByAgencyIdQuery(agencyId);
        List<AgencyDocumentResource> documentResources = agencyDocumentQueryService.handle(query).stream()
                .map(AgencyDocumentResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        if (documentResources.isEmpty()) {
            return ResponseEntity.ok(documentResources);
        }
        return ResponseEntity.ok(documentResources);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<AgencyDocumentResource> getDocumentById(@PathVariable Long agencyId, @PathVariable Long documentId) {
        var query = new GetAgencyDocumentByIdQuery(documentId);
        return agencyDocumentQueryService.handle(query)
                .map(document -> new ResponseEntity<>(AgencyDocumentResourceFromEntityAssembler.toResourceFromEntity(document), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}