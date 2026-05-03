package pe.edu.upc.travelmatch.agencies.domain.model.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAgencyDocumentCommand(
        @NotNull Long agencyId,
        @NotBlank String documentType,
        @NotBlank String documentUrl,
        String description
) {}