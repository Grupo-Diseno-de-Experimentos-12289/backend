package pe.edu.upc.travelmatch.agencies.domain.services;

import java.util.Optional;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyStaffCommand;

/** AgencyStaffCommandService contract. */
public interface AgencyStaffCommandService {

  /** Handle. */
  Optional<AgencyStaff> handle(CreateAgencyStaffCommand command);

  /** Handle. */
  Optional<AgencyStaff> handle(UpdateAgencyStaffCommand command);

  /** Handle. */
  void handle(DeleteAgencyStaffCommand command);
}
