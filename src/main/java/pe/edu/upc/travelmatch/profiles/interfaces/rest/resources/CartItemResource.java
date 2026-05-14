package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

import java.math.BigDecimal;

/** CartItemResource value carrier. */
public record CartItemResource(Long availabilityId, int quantity, BigDecimal price) {}
