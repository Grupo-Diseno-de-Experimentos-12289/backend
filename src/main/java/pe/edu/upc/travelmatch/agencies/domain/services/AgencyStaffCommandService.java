package pe.edu.upc.travelmatch.agencies.domain.services;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyStaffCommand;

import java.util.Optional;

public interface AgencyStaffCommandService {

    Optional<AgencyStaff> handle(CreateAgencyStaffCommand command);

    Optional<AgencyStaff> handle(UpdateAgencyStaffCommand command);

    void handle(DeleteAgencyStaffCommand command);
}