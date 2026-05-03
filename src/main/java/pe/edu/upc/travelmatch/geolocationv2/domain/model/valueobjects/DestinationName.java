package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record DestinationName(String name) {
    public DestinationName() {
        this(null);
    }

    public DestinationName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Destination name cannot be null or blank");
        }
    }
}

