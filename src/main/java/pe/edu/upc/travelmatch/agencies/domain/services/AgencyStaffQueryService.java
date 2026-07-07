package pe.edu.upc.travelmatch.agencies.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyStaffByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyStaffByAgencyIdQuery;

/** AgencyStaffQueryService contract. */
public interface AgencyStaffQueryService {
  /** Handle. */
  List<AgencyStaff> handle(GetAllAgencyStaffByAgencyIdQuery query);

  /** Handle. */
  Optional<AgencyStaff> handle(GetAgencyStaffByIdQuery query);
}
