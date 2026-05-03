package pe.edu.upc.travelmatch.agencies.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateAgencyDocumentResource(
        @NotBlank String documentType,
        @NotBlank String documentUrl,
        String description
) {}