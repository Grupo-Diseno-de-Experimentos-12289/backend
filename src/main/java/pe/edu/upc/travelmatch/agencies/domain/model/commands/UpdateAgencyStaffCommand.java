package pe.edu.upc.travelmatch.agencies.domain.model.commands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateAgencyStaffCommand(
        @NotNull Long id,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "\\d{9}", message = "Phone must be 9 digits") String phone,
        @NotBlank String position
) {}