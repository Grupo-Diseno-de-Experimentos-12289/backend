package pe.edu.upc.travelmatch.iam.interfaces.rest.resources;

/**
 * Sign in resource.
 *
 * @param email    the user email
 * @param password the user password
 */
public record SignInResource(String email, String password) {
}
