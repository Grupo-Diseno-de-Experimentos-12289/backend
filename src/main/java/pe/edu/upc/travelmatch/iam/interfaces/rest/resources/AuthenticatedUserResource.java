package pe.edu.upc.travelmatch.iam.interfaces.rest.resources;

import java.util.Set;

/**
 * Authenticated user resource.
 *
 * @param id    the user id
 * @param email the user email
 * @param token the authentication token
 * @param roles the user roles
 */
public record AuthenticatedUserResource(
    Long id,
    String email,
    String token,
    Set<String> roles
) {
}
