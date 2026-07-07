package pe.edu.upc.travelmatch.agencies.domain.model.commands;

import jakarta.validation.constraints.NotNull;

/** DeleteAgencyDocumentCommand value carrier. */
public record DeleteAgencyDocumentCommand(@NotNull Long id) {}
