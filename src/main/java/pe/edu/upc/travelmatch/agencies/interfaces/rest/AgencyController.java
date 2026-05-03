package pe.edu.upc.travelmatch.agencies.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgenciesQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyCommandService;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyQueryService;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.AgencyResourceFromAgencyAssembler;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.CreateAgencyCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyCommand;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping(value = "/api/v1/agencies", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agencies")
public class AgencyController {

    private final AgencyCommandService agencyCommandService;
    private final AgencyQueryService agencyQueryService;

    public AgencyController(AgencyCommandService agencyCommandService, AgencyQueryService agencyQueryService) {
        this.agencyCommandService = agencyCommandService;
        this.agencyQueryService = agencyQueryService;
    }

    @PostMapping
    public ResponseEntity<AgencyResource> createAgency(@RequestBody @Valid CreateAgencyResource resource) {
        try {
            var createAgencyCommand = CreateAgencyCommandFromResourceAssembler.toCommandFromResource(resource);
            Long agencyId = agencyCommandService.handle(createAgencyCommand);
            return agencyQueryService.handle(new GetAgencyByIdQuery(agencyId))
                    .map(AgencyResourceFromAgencyAssembler::toResourceFromEntity)
                    .map(agencyResource -> new ResponseEntity<>(agencyResource, HttpStatus.CREATED))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{agencyId}")
    public ResponseEntity<AgencyResource> getAgencyById(@PathVariable Long agencyId) {
        var getAgencyByIdQuery = new GetAgencyByIdQuery(agencyId);
        return agencyQueryService.handle(getAgencyByIdQuery)
                .map(AgencyResourceFromAgencyAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AgencyResource>> getAllAgencies() {
        var getAllAgenciesQuery = new GetAllAgenciesQuery();
        List<AgencyResource> agencyResources = agencyQueryService.handle(getAllAgenciesQuery).stream()
                .map(AgencyResourceFromAgencyAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agencyResources);
    }

    @PutMapping("/{agencyId}")
    public ResponseEntity<AgencyResource> updateAgency(@PathVariable Long agencyId, @RequestBody @Valid UpdateAgencyResource resource) {
        try {
            var updateAgencyCommand = new UpdateAgencyCommand(
                    agencyId,
                    resource.name(),
                    resource.description(),
                    resource.contactEmail(),
                    resource.contactPhone()
            );
            Agency updatedAgency = agencyCommandService.handle(updateAgencyCommand);
            return ResponseEntity.ok(AgencyResourceFromAgencyAssembler.toResourceFromEntity(updatedAgency));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{agencyId}")
    public ResponseEntity<?> deleteAgency(@PathVariable Long agencyId) {
        try {
            var deleteAgencyCommand = new DeleteAgencyCommand(agencyId);
            agencyCommandService.handle(deleteAgencyCommand);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}