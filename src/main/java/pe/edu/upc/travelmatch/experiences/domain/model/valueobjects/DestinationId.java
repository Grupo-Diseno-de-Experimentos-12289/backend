package pe.edu.upc.travelmatch.experiences.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record DestinationId(Long value) {
    public DestinationId {
        if (value == null || value < 0)
            throw new IllegalArgumentException("DestinationId must be a positive number");
    }

    public DestinationId() {
        this(0L);
    }
}