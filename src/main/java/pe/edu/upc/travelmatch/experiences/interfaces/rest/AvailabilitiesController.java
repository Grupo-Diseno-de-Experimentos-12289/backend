package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityTicketTypeCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityCommandService;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityQueryService;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityTicketTypeCommandService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.*;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.transform.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Availabilities", description = "Availability Management Endpoints")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AvailabilitiesController {

    private final AvailabilityCommandService availabilityCommandService;
    private final AvailabilityQueryService availabilityQueryService;
    private final AvailabilityTicketTypeCommandService availabilityTicketTypeCommandService;
    private final ExperienceRepository experienceRepository;
    private final AvailabilityRepository availabilityRepository;

    public AvailabilitiesController(
            AvailabilityCommandService availabilityCommandService,
            AvailabilityQueryService availabilityQueryService,
            AvailabilityTicketTypeCommandService availabilityTicketTypeCommandService,
            ExperienceRepository experienceRepository,
            AvailabilityRepository availabilityRepository
    ) {
        this.availabilityCommandService = availabilityCommandService;
        this.availabilityQueryService = availabilityQueryService;
        this.availabilityTicketTypeCommandService = availabilityTicketTypeCommandService;
        this.experienceRepository = experienceRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Operation(
            summary = "Create availability for an experience",
            description = "Creates a new availability entry for a given experience",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Availability created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping("/experiences/{experienceId}/availabilities")
    public ResponseEntity<Long> createAvailability(
            @PathVariable Long experienceId,
            @RequestBody CreateAvailabilityResource resource
    ) {
        var experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        CreateAvailabilityCommand command = new CreateAvailabilityCommand(
                experience,
                resource.startDateTime(),
                resource.endDateTime(),
                resource.capacity()
        );
        Long id = availabilityCommandService.handle(command);
        return ResponseEntity.ok(id);
    }

    @Operation(
            summary = "Update availability",
            description = "Updates an existing availability by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Availability updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid data")
            }
    )
    @PutMapping("/availabilities/{availabilityId}")
    public ResponseEntity<Void> updateAvailability(
            @PathVariable Long availabilityId,
            @RequestBody UpdateAvailabilityResource resource
    ) {
        UpdateAvailabilityCommand command = new UpdateAvailabilityCommand(
                availabilityId,
                resource.startDateTime(),
                resource.endDateTime(),
                resource.capacity()
        );
        availabilityCommandService.updateAvailability(command);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete availability",
            description = "Deletes a specific availability by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Availability deleted successfully")
            }
    )
    @DeleteMapping("/availabilities/{availabilityId}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long availabilityId) {
        availabilityCommandService.deleteAvailability(availabilityId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all availabilities",
            description = "Retrieves all availabilities in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of availabilities returned")
            }
    )
    @GetMapping("/availabilities")
    public ResponseEntity<List<AvailabilityResource>> getAllAvailabilities() {
        List<Availability> list = availabilityQueryService.getAllAvailabilities();
        var result = list.stream()
                .map(AvailabilityResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Create a ticket type for an availability",
            description = "Registers a new ticket type under a specific availability",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket type created successfully")
            }
    )
    @PostMapping("/availabilities/{availabilityId}/ticket-types")
    public ResponseEntity<Long> createTicketTypeForAvailability(
            @PathVariable Long availabilityId,
            @RequestBody CreateAvailabilityTicketTypeResource resource
    ) {
        var availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        CreateAvailabilityTicketTypeCommand command =
                CreateAvailabilityTicketTypeCommandFromResourceAssembler.toCommandFromResource(resource, availability);

        Long id = availabilityTicketTypeCommandService.handle(command);
        return ResponseEntity.ok(id);
    }
}