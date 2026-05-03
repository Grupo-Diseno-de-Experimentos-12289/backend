package pe.edu.upc.travelmatch.agencies.domain.services;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyCommand;

public interface AgencyCommandService {
    Long handle(CreateAgencyCommand command);
    Agency handle(UpdateAgencyCommand command);
    void handle(DeleteAgencyCommand command);
}