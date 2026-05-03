package pe.edu.upc.travelmatch.agencies.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyStaffByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyStaffByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyStaffCommandService;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyStaffQueryService;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyStaffResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyStaffResource;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyStaffResource; // Importación necesaria
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.AgencyStaffResourceFromEntityAssembler;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.CreateAgencyStaffCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.transform.UpdateAgencyStaffCommandFromResourceAssembler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/agencies/{agencyId}/staff", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agency Staff", description = "Operations related to Agency Staff")
public class AgencyStaffController {

    private final AgencyStaffCommandService agencyStaffCommandService;
    private final AgencyStaffQueryService agencyStaffQueryService;

    public AgencyStaffController(AgencyStaffCommandService agencyStaffCommandService, AgencyStaffQueryService agencyStaffQueryService) {
        this.agencyStaffCommandService = agencyStaffCommandService;
        this.agencyStaffQueryService = agencyStaffQueryService;
    }

    @PostMapping
    public ResponseEntity<AgencyStaffResource> createStaff(
            @PathVariable Long agencyId,
            @RequestBody @Valid CreateAgencyStaffResource resource) {
        try {
            var command = CreateAgencyStaffCommandFromResourceAssembler.toCommandFromResource(agencyId, resource);
            return agencyStaffCommandService.handle(command)
                    .map(staff -> new ResponseEntity<>(AgencyStaffResourceFromEntityAssembler.toResourceFromEntity(staff), HttpStatus.CREATED))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<AgencyStaffResource> updateStaff(
            @PathVariable Long agencyId,
            @PathVariable Long staffId,
            @RequestBody @Valid UpdateAgencyStaffResource resource) {
        if (!staffId.equals(resource.id())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            var command = UpdateAgencyStaffCommandFromResourceAssembler.toCommandFromResource(resource);
            return agencyStaffCommandService.handle(command)
                    .map(staff -> new ResponseEntity<>(AgencyStaffResourceFromEntityAssembler.toResourceFromEntity(staff), HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long agencyId, @PathVariable Long staffId) {
        try {
            var command = new DeleteAgencyStaffCommand(staffId);
            agencyStaffCommandService.handle(command);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AgencyStaffResource>> getAllStaffByAgencyId(@PathVariable Long agencyId) {
        var query = new GetAllAgencyStaffByAgencyIdQuery(agencyId);
        List<AgencyStaffResource> staffResources = agencyStaffQueryService.handle(query).stream()
                .map(AgencyStaffResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        if (staffResources.isEmpty()) {
            return ResponseEntity.ok(staffResources);
        }
        return ResponseEntity.ok(staffResources);
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<AgencyStaffResource> getStaffById(@PathVariable Long agencyId, @PathVariable Long staffId) {
        var query = new GetAgencyStaffByIdQuery(staffId);
        return agencyStaffQueryService.handle(query)
                .map(staff -> new ResponseEntity<>(AgencyStaffResourceFromEntityAssembler.toResourceFromEntity(staff), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}