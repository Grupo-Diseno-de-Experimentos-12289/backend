package pe.edu.upc.travelmatch.iam.interfaces.rest.resources;

import java.util.List;

/** SignUpResource value carrier. */
public record SignUpResource(
    String email,
    String password,
    String firstName,
    String lastName,
    String phone,
    List<String> roles) {}
