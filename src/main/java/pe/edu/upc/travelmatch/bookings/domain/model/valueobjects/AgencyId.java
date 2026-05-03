package pe.edu.upc.travelmatch.bookings.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record AgencyId(Long agencyId) {
    public AgencyId {
        if (agencyId < 0) {
            throw new IllegalArgumentException("Agency agencyId cannot be negative");
        }
    }
    public AgencyId() { this(0L); }
}
