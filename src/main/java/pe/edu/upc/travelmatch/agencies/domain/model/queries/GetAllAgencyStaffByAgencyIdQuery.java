package pe.edu.upc.travelmatch.agencies.domain.model.queries;

import jakarta.validation.constraints.NotNull;

/** GetAllAgencyStaffByAgencyIdQuery value carrier. */
public record GetAllAgencyStaffByAgencyIdQuery(@NotNull Long agencyId) {}
