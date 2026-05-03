package pe.edu.upc.travelmatch.agencies.domain.model.queries;

import jakarta.validation.constraints.NotNull;

public record GetAllAgencyStaffByAgencyIdQuery(
        @NotNull Long agencyId
) {}