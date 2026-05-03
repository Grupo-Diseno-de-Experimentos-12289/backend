package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record DestinationAddress(String address) {
    public DestinationAddress() {
        this(null);
    }

    public DestinationAddress {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Destination address cannot be null or blank");
        }
    }
}

