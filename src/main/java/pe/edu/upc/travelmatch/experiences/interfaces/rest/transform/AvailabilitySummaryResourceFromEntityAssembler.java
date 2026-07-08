package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.AvailabilitySummaryResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.AvailabilityTicketTypeSummaryResource;

/** Assembles an {@link AvailabilitySummaryResource} from an {@link Availability} entity. */
public class AvailabilitySummaryResourceFromEntityAssembler {

  private AvailabilitySummaryResourceFromEntityAssembler() {}

  /**
   * To resource from entity.
   *
   * @param entity the availability entity
   * @return the summarized availability resource
   */
  public static AvailabilitySummaryResource toResourceFromEntity(Availability entity) {
    var ticketTypes =
        entity.getTicketTypes().stream()
            .map(
                availabilityTicketType ->
                    new AvailabilityTicketTypeSummaryResource(
                        availabilityTicketType.getTicketType().getTicketTypeName(),
                        availabilityTicketType.getPrice(),
                        availabilityTicketType.getStock()))
            .toList();

    int totalStock = ticketTypes.stream().mapToInt(AvailabilityTicketTypeSummaryResource::stock).sum();

    return new AvailabilitySummaryResource(
        entity.getId(),
        entity.getStartDateTime(),
        entity.getEndDateTime(),
        entity.getCapacity(),
        totalStock,
        ticketTypes);
  }
}
