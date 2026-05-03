package pe.edu.upc.travelmatch.agencies.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAgencyDocumentResource(
        @NotNull Long id,
        @NotBlank String documentType,
        @NotBlank String documentUrl,
        String description
) {}