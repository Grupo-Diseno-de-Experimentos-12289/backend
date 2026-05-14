package pe.edu.upc.travelmatch.agencies.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** CreateAgencyStaffResource value carrier. */
public record CreateAgencyStaffResource(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank @Email String email,
    @NotBlank @Pattern(regexp = "\\d{9}", message = "Phone must be 9 digits") String phone,
    @NotBlank String position) {}
