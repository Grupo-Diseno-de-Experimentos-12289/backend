package pe.edu.upc.travelmatch.bookings.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.ExperiencesContextFacade;

import java.math.BigDecimal;

@Service("bookingExternalExperienceService")
public class ExternalExperienceService {

    private final ExperiencesContextFacade experiencesContextFacade;

    public ExternalExperienceService(ExperiencesContextFacade experiencesContextFacade) {
        this.experiencesContextFacade = experiencesContextFacade;
    }

    public boolean existsAvailabilityById(Long availabilityId) {
        return experiencesContextFacade.fetchAvailabilityInfo(availabilityId).isPresent();
    }

    public boolean hasSufficientStock(Long availabilityId, Long ticketTypeId, int quantity) {
        return experiencesContextFacade.isStockAvailable(availabilityId, ticketTypeId, quantity);
    }

    public BigDecimal getPriceForTicketType(Long availabilityId, Long ticketTypeId) {
        return experiencesContextFacade
                .fetchTicketPrice(availabilityId, ticketTypeId)
                .orElseThrow(() -> new IllegalArgumentException("TicketType price not found"));
    }

    public void decrementStock(Long availabilityId, Long ticketTypeId, int quantity) {
        experiencesContextFacade.decrementStock(availabilityId, ticketTypeId, quantity);
    }
}