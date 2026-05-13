package pe.edu.upc.travelmatch.agencies.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/** CreateAgencyDocumentResource value carrier. */
public record CreateAgencyDocumentResource(
    @NotBlank String documentType, @NotBlank String documentUrl, String description) {}
