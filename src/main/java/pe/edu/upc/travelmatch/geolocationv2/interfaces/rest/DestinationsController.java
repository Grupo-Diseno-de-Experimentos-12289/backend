package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.DeleteDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetAllDestinationsQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByIdQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.services.DestinationCommandService;
import pe.edu.upc.travelmatch.geolocationv2.domain.services.DestinationQueryService;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.CreateDestinationResource;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.DestinationResource;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform.CreateDestinationCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform.DestinationResourceFromEntityAssembler;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform.UpdateDestinationCommandFromResourceAssembler;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/destinations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Destinations", description = "Destination Management Endpoints")
public class DestinationsController {
    private final DestinationQueryService destinationQueryService;
    private final DestinationCommandService destinationCommandService;

    public DestinationsController(DestinationQueryService destinationQueryService, DestinationCommandService destinationCommandService) {
        this.destinationQueryService = destinationQueryService;
        this.destinationCommandService = destinationCommandService;
    }

    @PostMapping
    public ResponseEntity<DestinationResource> createDestination(@RequestBody CreateDestinationResource resource) {
        var createDestinationCommand = CreateDestinationCommandFromResourceAssembler.toCommandFromResource(resource);
        var destinationId = this.destinationCommandService.handle(createDestinationCommand);
        if (destinationId.equals(0L)) {
            return ResponseEntity.badRequest().build();
        }
        var getDestinationByIdQuery = new GetDestinationByIdQuery(destinationId);
        var optionalDestination = this.destinationQueryService.handle(getDestinationByIdQuery);
        var destinationResource = DestinationResourceFromEntityAssembler.toResourceFromEntity(optionalDestination.get());
        return new ResponseEntity<>(destinationResource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DestinationResource>> getAllDestinations() {
        var getAllDestinationsQuery = new GetAllDestinationsQuery();
        var destinations = this.destinationQueryService.handle(getAllDestinationsQuery);
        var destinationResources = destinations.stream()
                .map(DestinationResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(destinationResources);
    }

    @GetMapping("/{destinationId}")
    public ResponseEntity<DestinationResource> getDestinationById(@PathVariable Long
                                                                  destinationId) {
        var getDestinationByIdQuery = new GetDestinationByIdQuery(destinationId);
        var optionalDestination = this.destinationQueryService.handle(getDestinationByIdQuery);
        if (optionalDestination.isEmpty())
            return ResponseEntity.badRequest().build();
        var destinationResource = DestinationResourceFromEntityAssembler.toResourceFromEntity(optionalDestination.get());
        return ResponseEntity.ok(destinationResource);
    }

    @PutMapping("/{destinationId}")
    public ResponseEntity<DestinationResource> updateDestination(@PathVariable Long destinationId, @RequestBody DestinationResource resource) {
        var updateDestinationCommand = UpdateDestinationCommandFromResourceAssembler.toCommandFromResource(destinationId, resource);
        var optionalDestination = this.destinationCommandService.handle(updateDestinationCommand);
        if (optionalDestination.isEmpty())
            return ResponseEntity.badRequest().build();
        var destinationResource = DestinationResourceFromEntityAssembler.toResourceFromEntity(optionalDestination.get());
        return ResponseEntity.ok(destinationResource);
    }
    @DeleteMapping("/{destinationId}")
    public ResponseEntity<?> deleteDestination(@PathVariable Long destinationId) {
        var deleteDestinationCommand = new DeleteDestinationCommand(destinationId);
        this.destinationCommandService.handle(deleteDestinationCommand);
        return ResponseEntity.noContent().build();
    }
}
