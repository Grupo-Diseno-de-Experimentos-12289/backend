package pe.edu.upc.travelmatch.iam.domain.model.commands;

import java.util.List;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;

/** SignUpCommand value carrier. */
public record SignUpCommand(
    String email,
    String password,
    String firstName,
    String lastName,
    String phone,
    List<Role> roles) {}
