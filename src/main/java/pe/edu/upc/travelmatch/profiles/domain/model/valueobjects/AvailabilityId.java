package pe.edu.upc.travelmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record AvailabilityId(Long availabilityId) {
    public AvailabilityId {
        if(availabilityId < 0)
            throw new IllegalArgumentException("The availability id must be greater than zero");
    }
    public AvailabilityId(){
        this(0L);
    }
}
