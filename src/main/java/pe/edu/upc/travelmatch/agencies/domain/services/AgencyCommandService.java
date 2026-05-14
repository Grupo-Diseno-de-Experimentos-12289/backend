package pe.edu.upc.travelmatch.agencies.domain.services;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyCommand;

/** AgencyCommandService contract. */
public interface AgencyCommandService {
  /** Handle. */
  Long handle(CreateAgencyCommand command);

  /** Handle. */
  Agency handle(UpdateAgencyCommand command);

  /** Handle. */
  void handle(DeleteAgencyCommand command);
}
