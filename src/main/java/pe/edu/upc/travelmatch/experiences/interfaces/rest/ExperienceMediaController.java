package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceMediaCommandService;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceMediaQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateExperienceMediaResource;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.ExperienceMediaResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateExperienceMediaResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.transform.CreateExperienceMediaCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.transform.ExperienceMediaResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping("/api/v1/experience-media")
@Tag(name = "Experience Media", description = "Endpoints for media files")
public class ExperienceMediaController {

    private final ExperienceMediaCommandService commandService;
    private final ExperienceMediaQueryService queryService;
    private final ExperienceRepository experienceRepository;

    public ExperienceMediaController(
            ExperienceMediaCommandService commandService,
            ExperienceMediaQueryService queryService,
            ExperienceRepository experienceRepository
    ) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.experienceRepository = experienceRepository;
    }

    @Operation(
            summary = "Create media for an experience",
            description = "Registers a new media file (image/video) for a given experience",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Media created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or experience not found")
            }
    )
    @PostMapping("/experiences/{experienceId}/media")
    public ResponseEntity<Long> create(
            @PathVariable Long experienceId,
            @RequestBody CreateExperienceMediaResource resource
    ) {
        var experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        var command = CreateExperienceMediaCommandFromResourceAssembler
                .toCommandFromResource(resource, experience);

        var id = commandService.handle(command);
        return ResponseEntity.ok(id);
    }

    @Operation(
            summary = "Update media file",
            description = "Updates an existing experience media by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Media updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Media not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ExperienceMediaResource> update(@PathVariable Long id, @RequestBody UpdateExperienceMediaResource resource) {
        var command = new UpdateExperienceMediaCommand(id, resource.mediaUrl(), resource.caption());
        return commandService.handle(command)
                .map(ExperienceMediaResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get all experience media",
            description = "Retrieves all media files from all experiences",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of media returned")
            }
    )
    @GetMapping
    public ResponseEntity<List<ExperienceMediaResource>> getAll() {
        var result = queryService.getAll().stream()
                .map(ExperienceMediaResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Get media by experience",
            description = "Fetches all media for a specific experience ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Media list returned")
            }
    )
    @GetMapping("/experience/{experienceId}")
    public ResponseEntity<List<ExperienceMediaResource>> getByExperience(@PathVariable Long experienceId) {
        var result = queryService.findByExperienceId(experienceId).stream()
                .map(ExperienceMediaResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Delete media by ID",
            description = "Deletes a specific media file by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Media deleted successfully")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}