package pe.edu.upc.travelmatch.agencies.domain.model.queries;

import jakarta.validation.constraints.NotNull;

/** GetAllAgencyDocumentsByAgencyIdQuery value carrier. */
public record GetAllAgencyDocumentsByAgencyIdQuery(@NotNull Long agencyId) {}
