package pe.edu.upc.travelmatch.iam.interfaces.rest.resources;

import java.util.List;

/**
 * User resource.
 *
 * @param id        the user id
 * @param email     the user email
 * @param firstName the user first name
 * @param lastName  the user last name
 * @param phone     the user phone
 * @param roles     the user roles
 */
public record UserResource(
    Long id,
    String email,
    String firstName,
    String lastName,
    String phone,
    List<String> roles
) {
}
