package pe.edu.upc.travelmatch.iam.domain.model.commands;

/**
 * Sign in command.
 *
 * @param email    the email
 * @param password the password
 */
public record SignInCommand(String email, String password) {
}
