package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityTicketTypeCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateAvailabilityTicketTypeResource;

/** CreateAvailabilityTicketTypeCommandFromResourceAssembler. */
public class CreateAvailabilityTicketTypeCommandFromResourceAssembler {
  /** To command from resource. */
  public static CreateAvailabilityTicketTypeCommand toCommandFromResource(
      CreateAvailabilityTicketTypeResource resource, Availability availability) {
    return new CreateAvailabilityTicketTypeCommand(
        availability.getId(), resource.ticketTypeId(), resource.price(), resource.stock());
  }
}
