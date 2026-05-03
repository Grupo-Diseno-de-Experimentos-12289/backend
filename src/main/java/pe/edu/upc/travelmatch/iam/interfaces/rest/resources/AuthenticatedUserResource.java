package pe.edu.upc.travelmatch.iam.interfaces.rest.resources;

import java.util.Set;

public record AuthenticatedUserResource(Long id, String email, String token, Set<String> roles) {
}
