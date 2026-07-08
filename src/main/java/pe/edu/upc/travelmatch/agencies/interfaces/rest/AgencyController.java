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
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgenciesQuery;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyCommandService;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyQueryService;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.AgencyResourceFromAgencyAssembler;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.CreateAgencyCommandFromResourceAssembler;

/** AgencyController type. */
@RestController
@RequestMapping(value = "/api/v1/agencies", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agencies")
public class AgencyController {

  private final AgencyCommandService agencyCommandService;
  private final AgencyQueryService agencyQueryService;

  /** Constructs a new AgencyController. */
  public AgencyController(
      AgencyCommandService agencyCommandService, AgencyQueryService agencyQueryService) {
    this.agencyCommandService = agencyCommandService;
    this.agencyQueryService = agencyQueryService;
  }

  /** Create agency. */
  @PostMapping
  public ResponseEntity<AgencyResource> createAgency(
      @RequestBody @Valid CreateAgencyResource resource) {
    try {
      var createAgencyCommand =
          CreateAgencyCommandFromResourceAssembler.toCommandFromResource(resource);
      Long agencyId = agencyCommandService.handle(createAgencyCommand);
      return agencyQueryService
          .handle(new GetAgencyByIdQuery(agencyId))
          .map(AgencyResourceFromAgencyAssembler::toResourceFromEntity)
          .map(agencyResource -> ResponseEntity.status(HttpStatus.CREATED).body(agencyResource))
          .orElseGet(() -> ResponseEntity.internalServerError().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /** Get agency by id. */
  @GetMapping("/{agencyId}")
  public ResponseEntity<AgencyResource> getAgencyById(@PathVariable Long agencyId) {
    var getAgencyByIdQuery = new GetAgencyByIdQuery(agencyId);
    return agencyQueryService
        .handle(getAgencyByIdQuery)
        .map(AgencyResourceFromAgencyAssembler::toResourceFromEntity)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /** Get all agencies. */
  @GetMapping
  public ResponseEntity<List<AgencyResource>> getAllAgencies() {
    var getAllAgenciesQuery = new GetAllAgenciesQuery();
    List<AgencyResource> agencyResources =
        agencyQueryService.handle(getAllAgenciesQuery).stream()
            .map(AgencyResourceFromAgencyAssembler::toResourceFromEntity)
            .toList();
    return ResponseEntity.ok(agencyResources);
  }

  /** Update agency. */
  @PutMapping("/{agencyId}")
  public ResponseEntity<AgencyResource> updateAgency(
      @PathVariable Long agencyId, @RequestBody @Valid UpdateAgencyResource resource) {
    try {
      var updateAgencyCommand =
          new UpdateAgencyCommand(
              agencyId,
              resource.name(),
              resource.description(),
              resource.contactEmail(),
              resource.contactPhone());
      Agency updatedAgency = agencyCommandService.handle(updateAgencyCommand);
      return ResponseEntity.ok(
          AgencyResourceFromAgencyAssembler.toResourceFromEntity(updatedAgency));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /** Delete agency. */
  @DeleteMapping("/{agencyId}")
  public ResponseEntity<Void> deleteAgency(@PathVariable Long agencyId) {
    try {
      var deleteAgencyCommand = new DeleteAgencyCommand(agencyId);
      agencyCommandService.handle(deleteAgencyCommand);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
