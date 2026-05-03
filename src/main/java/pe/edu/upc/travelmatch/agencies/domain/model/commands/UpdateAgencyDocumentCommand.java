package pe.edu.upc.travelmatch.agencies.domain.model.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAgencyDocumentCommand(
        @NotNull Long id,
        @NotBlank String documentType,
        @NotBlank String documentUrl,
        String description
) {}