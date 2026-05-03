package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

import java.util.List;

public record CartResource(Long cartId, Long userId, List<CartItemResource> items) {
}
