package pe.edu.upc.travelmatch.iam.domain.model.commands;

import java.util.List;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;

/**
 * Sign up command.
 *
 * @param email     the email
 * @param password  the password
 * @param firstName the first name
 * @param lastName  the last name
 * @param phone     the phone
 * @param roles     the list of roles
 */
public record SignUpCommand(
    String email,
    String password,
    String firstName,
    String lastName,
    String phone,
    List<Role> roles) {
}
