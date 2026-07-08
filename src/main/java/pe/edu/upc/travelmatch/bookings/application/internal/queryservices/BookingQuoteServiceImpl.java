package pe.edu.upc.travelmatch.bookings.application.internal.queryservices;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.bookings.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingQuoteQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingQuote;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingQuoteService;

/** Service implementation for calculating booking quotes. */
@Service
public class BookingQuoteServiceImpl implements BookingQuoteService {

    private static final String DEFAULT_CANCELLATION_POLICY =
            "Cancelacion gratuita hasta 24 horas antes del inicio de la experiencia. "
                    + "Pasado ese plazo, la reserva no sera reembolsable.";

    private final ExternalExperienceService externalExperienceService;

    /**
     * Constructs a BookingQuoteServiceImpl.
     *
     * @param externalExperienceService the ACL service to the Experiences bounded context
     */
    public BookingQuoteServiceImpl(ExternalExperienceService externalExperienceService) {
        this.externalExperienceService = externalExperienceService;
    }

    @Override
    public BookingQuote handle(GetBookingQuoteQuery query) {
        if (!externalExperienceService.existsAvailabilityById(query.availabilityId())) {
            throw new IllegalArgumentException(
                    "Availability with id " + query.availabilityId() + " does not exist.");
        }

        boolean stockAvailable =
                externalExperienceService.hasSufficientStock(
                        query.availabilityId(), query.ticketTypeId(), query.quantity());

        BigDecimal unitPriceAmount =
                externalExperienceService.getPriceForTicketType(
                        query.availabilityId(), query.ticketTypeId());

        Money unitPrice = new Money(unitPriceAmount, "PEN");
        Money totalPrice = unitPrice.multiply(BigDecimal.valueOf(query.quantity()));

        return new BookingQuote(
                query.availabilityId(),
                query.ticketTypeId(),
                query.quantity(),
                unitPrice,
                totalPrice,
                stockAvailable,
                DEFAULT_CANCELLATION_POLICY);
    }
}