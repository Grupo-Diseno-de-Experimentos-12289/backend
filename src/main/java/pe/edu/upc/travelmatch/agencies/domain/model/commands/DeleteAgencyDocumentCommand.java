package pe.edu.upc.travelmatch.agencies.domain.model.commands;

import jakarta.validation.constraints.NotNull;

public record DeleteAgencyDocumentCommand(
        @NotNull Long id
) {}