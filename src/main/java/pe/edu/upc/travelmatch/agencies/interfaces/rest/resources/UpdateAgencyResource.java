package pe.edu.upc.travelmatch.agencies.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdateAgencyResource(
        String name,
        String description,
        String ruc,
        @Email String contactEmail,
        @Pattern(regexp = "\\d{9}", message = "Contact phone must be 9 digits") String contactPhone
) {}