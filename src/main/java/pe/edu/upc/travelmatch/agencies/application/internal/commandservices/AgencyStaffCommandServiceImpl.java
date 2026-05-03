package pe.edu.upc.travelmatch.agencies.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyStaffCommandService;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyStaffRepository;

import java.util.Optional;

@Service
public class AgencyStaffCommandServiceImpl implements AgencyStaffCommandService {

    private final AgencyStaffRepository agencyStaffRepository;
    private final AgencyRepository agencyRepository;

    public AgencyStaffCommandServiceImpl(AgencyStaffRepository agencyStaffRepository, AgencyRepository agencyRepository) {
        this.agencyStaffRepository = agencyStaffRepository;
        this.agencyRepository = agencyRepository;
    }

    @Override
    public Optional<AgencyStaff> handle(CreateAgencyStaffCommand command) {

        Optional<Agency> agency = agencyRepository.findById(command.agencyId());
        if (agency.isEmpty()) {
            throw new IllegalArgumentException("Agency with ID " + command.agencyId() + " does not exist.");
        }

        if (agencyStaffRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Staff with email " + command.email() + " already exists.");
        }

        var agencyStaff = new AgencyStaff(
                agency.get(),
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.position()
        );
        return Optional.of(agencyStaffRepository.save(agencyStaff));
    }

    @Override
    public Optional<AgencyStaff> handle(UpdateAgencyStaffCommand command) {

        Optional<AgencyStaff> existingStaff = agencyStaffRepository.findById(command.id());
        if (existingStaff.isEmpty()) {
            return Optional.empty();
        }

        Optional<AgencyStaff> staffWithSameEmail = agencyStaffRepository.findByEmail(command.email());
        if (staffWithSameEmail.isPresent() && !staffWithSameEmail.get().getId().equals(command.id())) {
            throw new IllegalArgumentException("Another staff with email " + command.email() + " already exists.");
        }

        AgencyStaff staffToUpdate = existingStaff.get();
        staffToUpdate.update(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.position()
        );
        return Optional.of(agencyStaffRepository.save(staffToUpdate));
    }

    @Override
    public void handle(DeleteAgencyStaffCommand command) {
        if (!agencyStaffRepository.existsById(command.id())) {
            throw new IllegalArgumentException("Staff with ID " + command.id() + " does not exist.");
        }
        agencyStaffRepository.deleteById(command.id());
    }
}