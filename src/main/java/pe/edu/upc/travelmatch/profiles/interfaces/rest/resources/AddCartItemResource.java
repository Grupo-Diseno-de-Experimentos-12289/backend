package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

import java.math.BigDecimal;

public record AddCartItemResource(Long availabilityId, Integer quantity, BigDecimal price) {
}
