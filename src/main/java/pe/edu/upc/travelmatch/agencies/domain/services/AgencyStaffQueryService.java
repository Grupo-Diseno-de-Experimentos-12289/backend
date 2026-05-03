package pe.edu.upc.travelmatch.agencies.domain.services;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyStaffByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyStaffByIdQuery;

import java.util.List;
import java.util.Optional;

public interface AgencyStaffQueryService {
    List<AgencyStaff> handle(GetAllAgencyStaffByAgencyIdQuery query);

    Optional<AgencyStaff> handle(GetAgencyStaffByIdQuery query);
}