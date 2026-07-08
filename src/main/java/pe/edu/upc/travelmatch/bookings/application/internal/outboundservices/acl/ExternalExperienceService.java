package pe.edu.upc.travelmatch.bookings.application.internal.outboundservices.acl;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.ExperiencesContextFacade;

/** ExternalExperienceService type. */
@Service("bookingExternalExperienceService")
public class ExternalExperienceService {

  private final ExperiencesContextFacade experiencesContextFacade;

  /** Constructs a new ExternalExperienceService. */
  public ExternalExperienceService(ExperiencesContextFacade experiencesContextFacade) {
    this.experiencesContextFacade = experiencesContextFacade;
  }

  /** Exists availability by id. */
  public boolean existsAvailabilityById(Long availabilityId) {
    return experiencesContextFacade.fetchAvailabilityInfo(availabilityId).isPresent();
  }

  /** Has sufficient stock. */
  public boolean hasSufficientStock(Long availabilityId, Long ticketTypeId, int quantity) {
    return experiencesContextFacade.isStockAvailable(availabilityId, ticketTypeId, quantity);
  }

  /** Get price for ticket type. */
  public BigDecimal getPriceForTicketType(Long availabilityId, Long ticketTypeId) {
    return experiencesContextFacade
        .fetchTicketPrice(availabilityId, ticketTypeId)
        .orElseThrow(() -> new IllegalArgumentException("TicketType price not found"));
  }

  /** Decrement stock. */
  public void decrementStock(Long availabilityId, Long ticketTypeId, int quantity) {
    experiencesContextFacade.decrementStock(availabilityId, ticketTypeId, quantity);
  }
}
