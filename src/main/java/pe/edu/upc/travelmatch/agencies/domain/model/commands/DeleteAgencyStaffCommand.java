package pe.edu.upc.travelmatch.agencies.domain.model.commands;

import jakarta.validation.constraints.NotNull;

/** DeleteAgencyStaffCommand value carrier. */
public record DeleteAgencyStaffCommand(@NotNull Long id) {}
