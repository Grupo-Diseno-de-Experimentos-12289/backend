package pe.edu.upc.travelmatch.agencies.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyCommandService;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.interfaces.acl.AgenciesContextFacade;

import java.util.Optional;

@Service
public class AgencyCommandServiceImpl implements AgencyCommandService {

    private final AgencyRepository agencyRepository;
    private final AgenciesContextFacade agenciesContextFacade;

    public AgencyCommandServiceImpl(AgencyRepository agencyRepository, AgenciesContextFacade agenciesContextFacade) {
        this.agencyRepository = agencyRepository;
        this.agenciesContextFacade = agenciesContextFacade;
    }

    @Override
    public Long handle(CreateAgencyCommand command) {

        if (!agenciesContextFacade.existsUserById(command.userId())) {
            throw new IllegalArgumentException("User with ID " + command.userId() + " does not exist in IAM.");
        }
        if (agencyRepository.existsByUserId(command.userId())) {
            throw new IllegalArgumentException("Agency already exists for user ID " + command.userId() + ".");
        }
        if (agencyRepository.existsByRuc(command.ruc())) {
            throw new IllegalArgumentException("Agency with RUC " + command.ruc() + " already exists.");
        }
        if (agencyRepository.existsByContactEmail(command.contactEmail())) {
            throw new IllegalArgumentException("Agency with contact email " + command.contactEmail() + " already exists.");
        }

        var agencyName = new AgencyName(command.name());
        var agency = new Agency(agencyName, command.description(), command.ruc(), command.contactEmail(), command.contactPhone(), command.userId());
        agencyRepository.save(agency);
        return agency.getId();
    }

    @Override
    public Agency handle(UpdateAgencyCommand command) {
        Optional<Agency> agencyOptional = agencyRepository.findById(command.agencyId());
        if (agencyOptional.isEmpty()) {
            throw new IllegalArgumentException("Agency with ID " + command.agencyId() + " does not exist.");
        }

        Agency agencyToUpdate = agencyOptional.get();

        if (command.contactEmail() != null && !command.contactEmail().equals(agencyToUpdate.getContactEmail()) &&
                agencyRepository.existsByContactEmailAndIdIsNot(command.contactEmail(), command.agencyId())) {
            throw new IllegalArgumentException("Another agency with contact email " + command.contactEmail() + " already exists.");
        }

        AgencyName updatedName = command.name() != null ? new AgencyName(command.name()) : null;
        agencyToUpdate.updateDetails(updatedName, command.description(), command.contactEmail(), command.contactPhone());
        agencyRepository.save(agencyToUpdate);
        return agencyToUpdate;
    }

    @Override
    public void handle(DeleteAgencyCommand command) {
        if (!agencyRepository.existsById(command.agencyId())) {
            throw new IllegalArgumentException("Agency with ID " + command.agencyId() + " does not exist.");
        }
        agencyRepository.deleteById(command.agencyId());
    }
}