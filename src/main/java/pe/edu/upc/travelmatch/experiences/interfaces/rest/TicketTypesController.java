package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllTicketTypesQuery;
import pe.edu.upc.travelmatch.experiences.domain.services.TicketTypeQueryService;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.TicketTypeResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.transform.TicketTypeResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/ticket-types", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Ticket Types", description = "Ticket Type Management Endpoints")
public class TicketTypesController {
    private final TicketTypeQueryService ticketTypeQueryService;

    public TicketTypesController(TicketTypeQueryService ticketTypeQueryService) {
        this.ticketTypeQueryService = ticketTypeQueryService;
    }

    @GetMapping
    public ResponseEntity<List<TicketTypeResource>> getAllTicketTypes() {
        var getAllTicketTypesQuery = new GetAllTicketTypesQuery();
        var ticketTypes = ticketTypeQueryService.handle(getAllTicketTypesQuery);
        var ticketTypeResources = ticketTypes.stream().map(TicketTypeResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(ticketTypeResources);
    }
}
