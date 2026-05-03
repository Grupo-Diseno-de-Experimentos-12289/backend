package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllTicketTypesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetTicketTypeByIdQuery;

import java.util.List;
import java.util.Optional;

public interface TicketTypeQueryService {
    List<TicketType> handle(GetAllTicketTypesQuery query);
    Optional<TicketType> handle(GetTicketTypeByIdQuery query);
}
