package pe.edu.upc.travelmatch.experiences.domain.services;
import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllTicketTypesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetTicketTypeByIdQuery;
/**
 * Service to manage TicketType queries.
 */
public interface TicketTypeQueryService {
  /**
   * Retrieves all the ticket types.
   *
   * @param query the command object
   * @return the list of ticket types
   */
  List<TicketType> handle(GetAllTicketTypesQuery query);
  /**
   * Handles the GetTicketTypeByIdQuery.
   *
   * @param query the query object
   * @return the optional ticket type
   */
  Optional<TicketType> handle(GetTicketTypeByIdQuery query);
}
