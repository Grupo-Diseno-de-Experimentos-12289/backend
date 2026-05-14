package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

import java.math.BigDecimal;

/** AddCartItemResource value carrier. */
public record AddCartItemResource(Long availabilityId, Integer quantity, BigDecimal price) {}
