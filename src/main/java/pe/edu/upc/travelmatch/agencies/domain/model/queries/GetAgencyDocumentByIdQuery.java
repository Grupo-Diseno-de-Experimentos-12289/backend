package pe.edu.upc.travelmatch.agencies.domain.model.queries;

import jakarta.validation.constraints.NotNull;

/** GetAgencyDocumentByIdQuery value carrier. */
public record GetAgencyDocumentByIdQuery(@NotNull Long documentId) {}
