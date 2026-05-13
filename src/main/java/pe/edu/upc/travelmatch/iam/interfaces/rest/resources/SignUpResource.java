package pe.edu.upc.travelmatch.iam.interfaces.rest.resources;

import java.util.List;

/**
 * Sign up resource.
 *
 * @param email     the user email
 * @param password  the user password
 * @param firstName the user first name
 * @param lastName  the user last name
 * @param phone     the user phone
 * @param roles     the user roles
 */
public record SignUpResource(
    String email,
    String password,
    String firstName,
    String lastName,
    String phone,
    List<String> roles
) {
}
